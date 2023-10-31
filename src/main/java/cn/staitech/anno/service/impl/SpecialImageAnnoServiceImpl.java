package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.special.SpecialAnnotation;
import cn.staitech.anno.domain.special.SpecialImage;
import cn.staitech.anno.domain.slide.SubImageVO;
import cn.staitech.anno.domain.specialimage.SpecialImageSelectVO;
import cn.staitech.anno.domain.specialimage.WaitSpecialImageVO;
import cn.staitech.anno.domain.specialimageanno.AnnoFeatures;
import cn.staitech.anno.domain.specialimageanno.AnnoMarkGeojson;
import cn.staitech.anno.domain.specialimageanno.AnnoProperties;
import cn.staitech.anno.domain.specialimageanno.SpecialAnnoAddVO;
import cn.staitech.anno.domain.specialimageanno.in.*;
import cn.staitech.anno.domain.specialsliceimage.AuditSpecialImageVO;
import cn.staitech.anno.domain.specialsliceimage.OrganDict;
import cn.staitech.anno.enums.SysDictTypeEnum;
import cn.staitech.anno.exception.AnnoException;
import cn.staitech.anno.mapper.SpecialAnnotationMapper;
import cn.staitech.anno.mapper.SpecialImageMapper;
import cn.staitech.anno.mapper.SubImageMapper;
import cn.staitech.anno.service.GetUserInformationService;
import cn.staitech.anno.service.SpecialImageAnnoService;
import cn.staitech.anno.service.SpecialImageService;
import cn.staitech.anno.utils.*;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.vividsolutions.jts.geom.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static cn.staitech.anno.constant.CommonConstant.GLIDE_LINE;


/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SpecialImageAnnoServiceImpl
 * @Description:
 * @date 2023年6月12日
 */
@Service
@Slf4j
public class SpecialImageAnnoServiceImpl implements SpecialImageAnnoService {
    DecimalFormat decimalFormat = new DecimalFormat("0.00000");
    @Resource
    private SpecialImageMapper specialImageMapper;
    @Resource
    private SubImageMapper subImageMapper;
    @Resource
    private SpecialAnnotationMapper specialAnnotationMapper;
    @Resource
    private SpecialImageService specialImageService;
    @Resource
    private GetUserInformationService getUserInformationService;

    /**
     * 求交集
     *
     * @param m 传入的集合
     * @param n 查询到的集合
     * @return
     */
    private static Long[] getJ(Long[] m, List<Long> n) {
        List<Long> a1 = Arrays.asList(m);
        List<Long> accountIdList = a1.stream().filter(n::contains).collect(Collectors.toList());
        Long[] arr = {};
        arr = accountIdList.toArray(arr);
        return arr;
    }

    /**
     * 求差集
     *
     * @param m 传入的集合
     * @param n 交集集合
     * @return
     */
    private static Long[] getC(Long[] m, Long[] n) {
        // 将较长的数组转换为set
        Set<Long> set = new HashSet<Long>(Arrays.asList(m.length > n.length ? m : n));

        // 遍历较短的数组，实现最少循环
        for (Long i : m.length > n.length ? n : m) {
            // 若是集合里有相同的就删掉，若是没有就将值添加到集合
            if (set.contains(i)) {
                set.remove(i);
            } else {
                set.add(i);
            }
        }

        Long[] arr = {};
        return set.toArray(arr);
    }

    @Override
    public int insertSpecialAnnotation(SpecialAnnotation anno) {
        return specialAnnotationMapper.insertSelective(anno);
    }

    @Override
    public int updatePointCount(SpecialAnnotation specialAnnotation) {
        return specialAnnotationMapper.updatePointCount(specialAnnotation);
    }

    @Override
    public PointCount selectCategoryCount(SpecialAnnotation anno) {
        return specialAnnotationMapper.selectCategoryCount(anno);
    }

    /**
     * 获取geojsonUrl
     *
     * @param geojsonUrls 文件地址,slideId 切片id
     * @return geojsonUrl
     */
    @Override
    public String getGeojsonUrls(String geojsonUrls, Long specialImageId) {
        String geojsonUrl;
        // 获取文件地址是否存在
        if (geojsonUrls == null || geojsonUrls.isEmpty()) {

            geojsonUrl = FileUtils.createFile(specialImageId);
            return geojsonUrl;
        }
        geojsonUrl = geojsonUrls;
        return geojsonUrl;
    }

    @Override
    public int update(SpecialAnnotation ann) {
        return specialAnnotationMapper.updateByPrimaryKeySelective(ann);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateSpecialImageList(AuditSpecialImageVO vo) {
        Long[] imageIds = vo.getSpecialImageIds();
        if (null == imageIds) {
            return R.fail(MessageSource.M("DATA_NULL"));
        }
        //参数校验
        //审核状态 0：待审核 1：审核通过 2：审核不通过
        int auditStatus = vo.getAuditStatus();
        Map paramMap = new HashMap<>(16);
		/*if(auditStatus == 1){
			//确保所选切片全部是待审核或者审核通过的数据
			paramMap.put("auditSucess", auditStatus);
		}else if(auditStatus == 2){
			//确保所选切片全部是待审核或者审核通过的数据
			paramMap.put("auditFail", auditStatus);
		}*/
        paramMap.put("auditStatus", 0);
        paramMap.put("specialImageIds", imageIds);
        paramMap.put("specialId", vo.getSpecialId());
        List<SpecialImage> list = specialImageMapper.selectSpecialImageListByParm(paramMap);
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.size() != imageIds.length) {
                return R.fail(MessageSource.M("DATA_NULL"));
            }
        } else {
            return R.fail(MessageSource.M("DATA_NULL"));
        }

        SpecialImage record = new SpecialImage();
        BeanUtils.copyProperties(vo, record);
        record.setAuditTime(DateUtil.date());
        //审核状态 2：审核不通过
        if (auditStatus == 2) {
            //切图状态改为未切图
            record.setSliceImageStatus(0);
        }
        record.setUpdateTime(DateUtil.date());
        record.setUpdateBy(SecurityUtils.getUserId());
        //主图修改状态
        specialImageMapper.updateByPrimaryKeySelective(record);

        //针对审核通过的数据，需要根据主图imageid、专题id、批次id查询对应的tb_sub_image所有小的切图，修改审核状态为通过
        //审核状态 1：审核通过
        if (auditStatus == 1) {
            List<Long> allSubIds = new ArrayList<>();
            for (SpecialImage sImage : list) {
                //根据主图imageid、专题id、批次id查询对应的tb_sub_image所有小的切图，修改审核状态为通过更新小图审核状态
                //查询小图列表
                Map<String, Object> columnMap = new HashMap<>(16);
                columnMap.put("parent_image_id", sImage.getImageId());
                columnMap.put("special_id", sImage.getSpecialId());
                columnMap.put("slice_batch_number", sImage.getSliceBatchNumber());
                List<SubImage> subList = subImageMapper.selectByMap(columnMap);
                if (CollectionUtils.isNotEmpty(subList)) {
                    for (SubImage simage : subList) {
                        allSubIds.add(simage.getImageId());
                    }
                }
            }

            //批量修改小图状态
            SubImageVO simage = new SubImageVO();
            simage.setImageIds(allSubIds);
            simage.setAuditStatus(auditStatus);
            simage.setUpdateTime(DateUtil.date());
            simage.setUpdateBy(SecurityUtils.getUserId());
            subImageMapper.updateByPrimaryKeySelective(simage);
        }
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public R<String> updateDeliveryBySpecialId(AuditSpecialImageVO vo) {
        //参数校验
        Map paramMap = new HashMap<>(16);
        paramMap.put("auditFail", 1);
        paramMap.put("specialId", vo.getSpecialId());
        List<SpecialImage> list = specialImageMapper.selectSpecialImageListByParm(paramMap);
        if (CollectionUtils.isEmpty(list)) {
            SpecialImage record = new SpecialImage();
            BeanUtils.copyProperties(vo, record);
            record.setAuditStatus(1);
            record.setUpdateTime(DateUtil.date());
            record.setUpdateBy(SecurityUtils.getUserId());
            record.setDeliveryStatus(1);
            specialImageMapper.updateByPrimaryKeySelective(record);
        } else {
            return R.fail(MessageSource.M("OPERATE_ERROR"));
        }
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
    }

    /**
     * @param @param  specialImageSelectVO
     * @param @return
     * @return List<WaitSpecialImageVO>
     * @throws
     * @Title: selectWaitSpecialImage
     * @Description: 查询全部待选的专题列表（根据切片编号/上传时间/添加状态查询)
     */

    @Override
    public List<WaitSpecialImageVO> selectWaitSpecialImage(SpecialImageSelectVO specialImageSelectVO) {
        return specialImageMapper.selectWaitSpecialImage(specialImageSelectVO);
    }

    @Override
    public List<SpecialAnnotation> selectSpecialAnnotationList(SpecialAnnotation annotation) {
        return specialAnnotationMapper.selectSpecialAnnotationList(annotation);
    }

    @Override
    public List<SpecialAnnoProperties> selectSpecialPropertiesList(SpecialAnnotation annotation) {
        return specialAnnotationMapper.selectSpecialPropertiesList(annotation);
    }

    @Override
    public AnnoMarkGeojson getMarkGeojsonByList(List<SpecialAnnotation> list) {
        AnnoMarkGeojson markGeojson = new AnnoMarkGeojson();
        markGeojson.setType("FeatureCollection");
        //		List<Features> featuresList = new ArrayList<>();
        List<AnnoFeatures> featuresList = new ArrayList<>();
        for (SpecialAnnotation anno : list) {
            //			Features feature =  new Features();
            AnnoFeatures feature = new AnnoFeatures();
            feature.setId(anno.getAnnotationId());
            feature.setType("Feature");

            //geojson处理
            String geometry = anno.getGeometry();
            if (StringUtils.isNotEmpty(geometry)) {
                JSONObject geometryJsonObject = JSONObject.parseObject(geometry);
                feature.setGeometry(geometryJsonObject);
            } else {
                feature.setGeometry(null);
            }


            //			Properties properties = new Properties();
            //			BeanUtils.copyProperties(anno, properties);
            AnnoProperties properties = new AnnoProperties();
            properties = getPropertiesBy(anno);
            String label_color = null;
            String label_name = null;
            if (anno.getCategoryId() != null) {
                //根据标注id获取标注类别详情
                //取缓存字典遍历id,匹配颜色
                List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                    for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                        if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(anno.getCategoryId()))) {
                            label_color = dictData.getColor();
                            label_name = dictData.getDictLabel();
                            properties.setLabel_color(label_color);
                            properties.setLabel_name(label_name);
                            break;
                        }
                    }
                }
            }

            SysUser sysUser = getUserInformationService.selectById(anno.getCreateBy());
            properties.setAnnotation_owner(sysUser.getUserName());
            properties.setMarking_id(anno.getSliceAnnotationId());
            //add properties
            feature.setProperties(properties);
            featuresList.add(feature);
        }
        markGeojson.setFeatures(featuresList);
        return markGeojson;
    }

    @Override
    public SpecialAnnotation selectByPrimaryKey(Long specialAnnotationId) {
        return specialAnnotationMapper.selectByPrimaryKey(specialAnnotationId);
    }

    @Override
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateAnnotation(SpecialAnnotation anno) {
        specialAnnotationMapper.updateByPrimaryKeySelective(anno);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    @Override
    public int deleteAnnotationById(Long annotationId) {
        return specialAnnotationMapper.deleteByPrimaryKey(annotationId);
    }

    @Override
    public List<OrganDict> getSystemDict(OrganDict dict) {
        return specialAnnotationMapper.getSystemDict(dict);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<SpecialAnnoAddVO>> annotationSave(List<SpecialAnnoAddVO> annoList, SpecialImage sImage) throws Exception {
        List<SpecialAnnoAddVO> retList = new ArrayList<>();
        int flag = 1;
        for (SpecialAnnoAddVO annotation : annoList) {
            SpecialAnnoAddVO saav = new SpecialAnnoAddVO();
            //操作类型 1:save  2:update 3:delte
            Long operateType = annotation.getOperateType();
            if (operateType == 2 || operateType == 3) {
                if (annotation.getAnnotationId() == null) {
                    flag = -1;
                    break;
                }
            }

            SpecialAnnotation stAnnotation = new SpecialAnnotation();
            BeanUtils.copyProperties(annotation, stAnnotation);
            //操作类型 1:save  2:update 3:delte
            if (operateType == 1 || operateType == 2) {
                //				Long userId = annotation.getMarkingCreateBy();
                Long userId = SecurityUtils.getUserId();
                if (StringUtils.isNotEmpty(annotation.getLocation())) {
                    // 判断新图像是否符合规则
                    Geometry geometry = MarkVerify.addVerify(annotation.getLocation());
                    if (geometry.isEmpty()) {
                        flag = -1;
                        break;
                    }
                    double area = geometry.getArea();
                    double length = geometry.getLength();
                    stAnnotation.setMeasure(new BigDecimal(decimalFormat.format(area)));
                    stAnnotation.setPerimeter(new BigDecimal(decimalFormat.format(length)));
                }


                stAnnotation.setImageId(sImage.getImageId());
                stAnnotation.setSpecialId(sImage.getSpecialId());
                stAnnotation.setCategoryId(annotation.getCategoryId());
				/*if (annotation.getCategoryId() != 0) {
					stAnnotation.setCreateCategoryId(userId);
				}*/

                //操作类型 1:save  2:update 3:delte
                if (operateType == 1) {
                    stAnnotation.setCreateBy(userId);
                    stAnnotation.setCreateTime(DateUtil.date());
                    Object oldAnnotationId = stAnnotation.getOldAnnotationId();
                    //					Object oldAnnotationId = null;
                    int status = specialAnnotationMapper.insertSelective(stAnnotation);
                    //操作后状态 1:失败  2:成功
                    if (status > 0) {
                        BeanUtils.copyProperties(stAnnotation, saav);
                        saav.setAnnotationId(stAnnotation.getSliceAnnotationId());
                        saav.setOldAnnotationId(oldAnnotationId);
                        saav.setOperateStatus(2);
                        saav.setOperateType(operateType);
                        //创建人
                        SysUser annotationBy = getUserInformationService.selectById(userId);
                        if (annotationBy != null) {
                            saav.setCreateName(annotationBy.getNickName());
                        }
                        //标签颜色
                        long categoryId = annotation.getCategoryId();
                        //取缓存字典遍历id,匹配颜色
                        List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                            for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                                if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(categoryId))) {
                                    saav.setColor(dictData.getColor());
                                }
                            }
                        }
                    }
                    retList.add(saav);
                } else if (operateType == 2) {
                    stAnnotation.setUpdateBy(userId);
                    stAnnotation.setUpdateTime(DateUtil.date());
                    stAnnotation.setSliceAnnotationId(Long.valueOf(stAnnotation.getAnnotationId()));
                    //					stAnnotation.setSliceAnnotationId(null);
                    int status = specialAnnotationMapper.updateByPrimaryKeySelective(stAnnotation);
                    //操作后状态 1:失败  2:成功
                    if (status > 0) {
                        //						SpecialAnnotation specialAnnotation = specialAnnotationMapper.selectByPrimaryKey(stAnnotation.getSliceAnnotationId());
                        SpecialAnnotation specialAnnotation = new SpecialAnnotation();
                        specialAnnotation.setSliceAnnotationId(stAnnotation.getSliceAnnotationId());
                        List<SpecialAnnotation> sliceAnnoList = specialAnnotationMapper.selectSpecialAnnotationList(specialAnnotation);
                        specialAnnotation = sliceAnnoList.get(0);
                        BeanUtils.copyProperties(specialAnnotation, saav);
                        saav.setOperateStatus(2);
                        saav.setOperateType(operateType);
                        saav.setOldAnnotationId(stAnnotation.getOldAnnotationId());
                        //						saav.setOldAnnotationId(null);
                        //创建人
                        SysUser annotationBy = getUserInformationService.selectById(specialAnnotation.getCreateBy());
                        stAnnotation.setAnnotationId(String.valueOf(specialAnnotation.getSliceAnnotationId()));
                        //						stAnnotation.setAnnotationId(null);
                        if (annotationBy != null) {
                            saav.setCreateName(annotationBy.getNickName());
                        }
                        //修改人
                        SysUser updateAannotation = getUserInformationService.selectById(userId);
                        if (updateAannotation != null) {
                            saav.setUpdateName(updateAannotation.getNickName());
                        }
                        //标签颜色
                        long categoryId = specialAnnotation.getCategoryId();
                        //取缓存字典遍历id,匹配颜色
                        List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                            for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                                if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(categoryId))) {
                                    saav.setColor(dictData.getColor());
                                }
                            }
                        }
                    }
                    retList.add(saav);
                }
            }

            //操作类型 1:save  2:update 3:delete
            if (operateType == 3) {
                //				int status = specialAnnotationMapper.deleteByPrimaryKey(stAnnotation.getAnnotationId());
                int status = specialAnnotationMapper.deleteByPrimaryKey(null);
                //操作后状态 1:失败  2:成功
                if (status > 0) {
                    saav.setOperateType(operateType);
                    saav.setOldAnnotationId(stAnnotation.getOldAnnotationId());
                    //					saav.setOldAnnotationId(null);
                    saav.setOperateStatus(2);
                }
                retList.add(saav);
            }
        }
        if (flag == -1) {
            return R.fail(MessageSource.M("GRAPHICS_MARK_NOT_RULES"));
        }
        return R.ok(retList, MessageSource.M("OPERATE_SUCCEED"));
    }


    /**
     * 组装数据
     */
    @Override
    public AnnoProperties getPropertiesBy(SpecialAnnotation req) {
        AnnoProperties properties = new AnnoProperties();
        if (null != req.getCreateBy()) {
            SysUser sysUser = getUserInformationService.selectById(req.getCreateBy());
            properties.setAnnotation_owner(sysUser.getUserName());
        }

        if (null != req.getUpdateBy()) {
            SysUser sysUser = getUserInformationService.selectById(req.getUpdateBy());
            properties.setAnnotation_update_owner(sysUser.getUserName());
        }


        if (null != req.getSliceAnnotationId()) {
            properties.setMarking_id(req.getSliceAnnotationId());
        }

        if (null != req.getCreateTime()) {
            properties.setCreate_time(DateUtil.format(req.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        }


        if (StringUtils.isNotEmpty(req.getAnnotationType())) {
            properties.setAnnotation_type(req.getAnnotationType());
        }

        if (StringUtils.isNotEmpty(req.getLocationType())) {
            properties.setLocation_type(req.getLocationType());
        }
        if (StringUtils.isNotEmpty(req.getArea())) {
            properties.setArea(req.getArea());
        }

        if (null != req.getPerimeter()) {
            properties.setPerimeter(String.valueOf(req.getPerimeter()));
        }
        if (StringUtils.isNotEmpty(req.getDescription())) {
            properties.setDescription(req.getDescription());
        }

        if (StringUtils.isNotEmpty(req.getRadius())) {
            properties.setRadius(req.getRadius());
        }

        if (null != req.getCategoryId()) {
            properties.setCategory_id(req.getCategoryId().longValue());
        }


        return properties;
    }

    @Override
    public void callBackAnnoResult(AlgorithmAnnIn algorithmAnnIn) {
        // 查询切片表中切片信息
        SpecialImage specialImage = specialImageService.selectByPrimaryKey(algorithmAnnIn.getSpecialImageId());
        if (null != specialImage) {
            saveAnn(algorithmAnnIn);
        } else {
            log.info("专题图片信息不存在，" + algorithmAnnIn.getSpecialImageId());
        }
    }


    public void saveAnn(AlgorithmAnnIn req) {
        List<AlgorithmGeometry> geometryList = req.getGeometryList();
        if (CollectionUtils.isNotEmpty(geometryList)) {
            for (int i = 0; i < geometryList.size(); i++) {
                SpecialAnnotation specialAnnotation = new SpecialAnnotation();
                specialAnnotation.setSpecialId(req.getSpecialId());
                specialAnnotation.setSpecialImageId(req.getSpecialImageId());
                specialAnnotation.setImageId(req.getImageId());
                AlgorithmGeometry aGeomety = geometryList.get(i);
                String numKey = req.getSpecialImageId() + GLIDE_LINE + "";
                // 获取标注名称 null278_null_大脑切面1
                String measure_full_name = "" + numKey + GLIDE_LINE;
                //				String label_color = null;
                String label_name = null;
                int categoryId = -1;
                String categoryname = aGeomety.getCategoryname();
                if (StringUtils.isNotEmpty(categoryname)) {
                    //取缓存字典遍历id,匹配颜色
                    List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                        for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                            if (dictData.getRemark().equalsIgnoreCase(categoryname)) {
                                //								label_color = dictData.getColor();
                                label_name = dictData.getRemark();
                                categoryId = Integer.valueOf(dictData.getDictValue());
                                measure_full_name += label_name;
                                break;
                            }
                        }
                    }
                }
                String annotationId = CustomizationIdUtils.getSdId();
                specialAnnotation.setAnnotationId(annotationId);
                specialAnnotation.setCreateBy(1L);
                specialAnnotation.setMeasureFullName(measure_full_name);
                specialAnnotation.setAnnotationType("AI");

                specialAnnotation.setCategoryId(categoryId);

                if (null != aGeomety.getGeometry()) {
                    JSONObject jsonobject = aGeomety.getGeometry();
                    String typeStr = jsonobject.getString("type");
                    if (StringUtils.isNotEmpty(typeStr)) {
                        specialAnnotation.setLocationType(typeStr);
                    }
                    specialAnnotation.setGeometry(aGeomety.getGeometry().toString());
                }

                if (StringUtils.isNotEmpty(aGeomety.getArea())) {
                    specialAnnotation.setArea(aGeomety.getArea());
                }
                if (StringUtils.isNotEmpty(aGeomety.getPerimeter())) {
                    specialAnnotation.setPerimeter(new BigDecimal(aGeomety.getPerimeter()));
                }

                if (StringUtils.isEmpty(aGeomety.getArea()) || StringUtils.isEmpty(aGeomety.getPerimeter())) {
                    String geometryStr = WktUtil.jsonToWkt(aGeomety.getGeometry());
                    Geometry geometry = null;
                    try {
                        geometry = MarkVerify.addVerify(geometryStr);
                        if (null != geometry) {
                            Double area = geometry.getArea();
                            Double length = geometry.getLength();
                            specialAnnotation.setMeasure(new BigDecimal(decimalFormat.format(area)));
                            specialAnnotation.setPerimeter(new BigDecimal(decimalFormat.format(length)));
                        }
                    } catch (AnnoException e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
                insertSpecialAnnotation(specialAnnotation);
            }
        }
    }

    @Override
    public R callBackSlideViscer(List<CallBackAnnAddIn> list) {
        // TODO Auto-generated method stub
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            for (CallBackAnnAddIn vo : list) {
                // 查询切片表中切片信息
                SpecialImage specialImage = specialImageService.selectByPrimaryKey(vo.getSpecialImageId());
                if (specialImage == null) {
                    return R.fail(MessageSource.M("NO_SLIDE_DATA"));
                }
                SpecialAnnotation specialAnnotation = new SpecialAnnotation();
                BeanUtils.copyProperties(vo, specialAnnotation);
                specialAnnotation = getTransByCallBackAnnAddIn(vo);
                List<CallBackAnnGeometry> geometryList = vo.getGeometryList();
                specialAnnotation.setCreateBy(SecurityUtils.getUserId());
                specialAnnotation.setCreateTime(DateUtil.date());
                specialAnnotation.setCreateBy(0L);
                specialAnnotation.setAnnotationType("AI");
                int dictValue = -1;
                for (CallBackAnnGeometry annGeometry : geometryList) {
                    if (StringUtils.isNotEmpty(annGeometry.getCategory_name())) {
                        //取缓存字典遍历id,匹配颜色
                        List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
                        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                            for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                                if (dictData.getDictLabel().equalsIgnoreCase(annGeometry.getCategory_name())) {
                                    dictValue = Integer.valueOf(dictData.getDictValue());
                                    break;
                                }
                            }
                        }
                    }
                    specialAnnotation.setCategoryId(Integer.valueOf(dictValue));
                    //					specialAnnotation.setLocationType(annGeometry.getLocation_type());
                    annGeometry.setGeometry(annGeometry.getGeometry());
                    String annotationId = CustomizationIdUtils.getSdId();
                    specialAnnotation.setAnnotationId(annotationId);

                    String geometryStr = WktUtil.jsonToWkt(annGeometry.getGeometry());
                    Geometry geometry = null;
                    try {
                        geometry = MarkVerify.addVerify(geometryStr);
                        if (null != geometry) {
                            double area = geometry.getArea();
                            double length = geometry.getLength();
                            specialAnnotation.setMeasure(new BigDecimal(decimalFormat.format(area)));
                            specialAnnotation.setPerimeter(new BigDecimal(decimalFormat.format(length)));
                        }
                    } catch (AnnoException e) {
                        e.printStackTrace();
                    } finally {

                    }
                    // 添加数据库，添加后返回自增id
                    insertSpecialAnnotation(specialAnnotation);
                }
            }
        }
        return R.ok();

    }
    public SpecialAnnotation getTransByCallBackAnnAddIn(CallBackAnnAddIn req) {
        SpecialAnnotation specialAnnotation = new SpecialAnnotation();
        if (null != req.getSpecialImageId()) {
            specialAnnotation.setSpecialImageId(req.getSpecialImageId());
        }
        if (null != req.getSpecialId()) {
            specialAnnotation.setSpecialId(req.getSpecialId());
        }

        if (null != req.getImageId()) {
            specialAnnotation.setImageId(req.getImageId());
        }
        return specialAnnotation;
    }

}
