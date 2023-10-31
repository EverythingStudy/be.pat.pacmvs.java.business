package cn.staitech.anno.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.enums.SysDictTypeEnum;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.SpecialImageMapper;
import cn.staitech.anno.mapper.SubImageMapper;
import cn.staitech.anno.service.SpecialImageService;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.WktUtil;
import cn.staitech.anno.vo.slide.SubImageVO;
import cn.staitech.anno.vo.special.Special;
import cn.staitech.anno.vo.special.SpecialAnnotation;
import cn.staitech.anno.vo.special.SpecialImage;
import cn.staitech.anno.vo.specialimage.InsertSpecialImageVO;
import cn.staitech.anno.vo.specialimage.SpecialImageSelectVO;
import cn.staitech.anno.vo.specialimage.SpecialImageVO;
import cn.staitech.anno.vo.specialimage.WaitSpecialImageVO;
import cn.staitech.anno.vo.specialimageanno.AlgorithmCutImageVO;
import cn.staitech.anno.vo.specialimageanno.SpecialAnnDataVO;
import cn.staitech.anno.vo.specialimageanno.SpecialCutImageVO;
import cn.staitech.anno.vo.specialsliceimage.AuditSpecialImageVO;
import cn.staitech.anno.vo.subimage.SubImage;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.uuid.IdUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 专题选片表 服务实现类
 * </p>
 *
 * @author wanglibei
 * @since 2023-06-02
 */
@Slf4j
@Service
public class SpecialImageServiceImpl implements SpecialImageService {

    @Resource
    private SpecialImageMapper specialImageMapper;


    @Resource
    private SubImageMapper subImageMapper;


    @Resource
    private SpecialService specialService;

    @Resource
    private SubImageService subImageService;

    @Resource
    private ImageMapper imageMapper;

    /**
     *
     * @Title: insertSpecialImageList
     * @Description: 批量插入专题选片列表
     * @param @param specialImageList
     * @param @return
     * @return int
     * @throws
     */

    /**
     * @param @param  o
     * @param @return
     * @return Object
     * @throws
     * @Title: getCoordinates
     * @Description: 坐标处理
     */
    public static List getCoordinates(List o) {
        for (int i = 0; i < o.size(); i++) {
            Object s = o.get(i);
            if (s instanceof Collection<?>) {
                List pList = (List) s;
                getCoordinates(pList);
            } else {
                int index = o.indexOf(s);
                int a4_1 = getIntByPoint(s);
                o.set(index, a4_1);
            }
        }
        return o;
    }

    public static Integer getIntByPoint(Object param) {
        Integer outPut = 0;
        if (param instanceof String) {
            outPut = (Integer) param;
        } else if (param instanceof Integer) {
            outPut = (Integer) param;
        } else if (param instanceof Double) {
            double d = ((Double) param).doubleValue();
            double d_1 = Math.round(d);
            outPut = (int) d_1;
        } else if (param instanceof Float) {
            float f = ((Float) param).floatValue();
            double f_1 = Math.round(f);
            outPut = (int) f_1;
        } else if (param instanceof Long) {
            outPut = (Integer) param;
        } else if (param instanceof BigDecimal) {
//			outPut =  ((BigDecimal) param).intValue();
            BigDecimal decimal = (BigDecimal) param;
            outPut = Math.round(decimal.floatValue());
        }
        outPut = Math.abs(outPut);
        return outPut;
    }

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

    /**
     * 根据专题查看所有的图片
     *
     * @param specialId
     * @return
     */
    @Override
    public List<SpecialImage> selectSpecialId(Long specialId) {
        return specialImageMapper.selectSpecialId(specialId);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public R<String> insertSpecialImageList(InsertSpecialImageVO vo) throws Exception {

        Long[] imageIds = vo.getImageIdList();
        List<Long> newList = new ArrayList<Long>();
        for (Long imageId : imageIds) {
            if (!newList.contains(imageId)) {
                Map paramAgainMap = new HashMap<>(16);
                paramAgainMap.put("imageId", imageId);
                paramAgainMap.put("specialId", vo.getSpecialId());
                List<SpecialImage> checkList = specialImageMapper.selectSpecialImageListByParm(paramAgainMap);
                if (org.apache.commons.collections.CollectionUtils.isEmpty(checkList)) {
                    newList.add(imageId);
                }
            }
        }

        List<SpecialImage> list = new ArrayList<>();
        if (null != newList && newList.size() > 0) {
            for (int i = 0; i < newList.size(); i++) {
                //			判断是否已经被添加过，如果被添加过就不添加了
                SpecialImage image = new SpecialImage();
                image.setImageId(newList.get(i));
                image.setSpecialId(vo.getSpecialId());
                image.setCreateTime(DateUtil.date());
                image.setTopicId(vo.getTopicId());
                //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中,确保在绘制中可以进行修改，且提交人是绘制
                image.setCreateBy(0L);
                image.setSliceImageStatus(3);
                //TODO 直接修改状态为绘制中+绘制人为AI
                list.add(image);
            }

        }
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
    }


    @Override
    public void cutImageNotice(SpecialCutImageVO resData) {
        SpecialImage specialImage = resData.getImage();
        Long imageId = specialImage.getImageId();
        Image imageInfo = imageMapper.selectById(imageId);
        String imageName = imageInfo.getImageName();
        List<SpecialAnnotation> annoList = resData.getAnnoList();
        AlgorithmCutImageVO cutVo = new AlgorithmCutImageVO();
        cutVo.setSpecialImageId(specialImage.getSpecialImageId());
        cutVo.setImageId(imageId);
        cutVo.setFilePath(imageInfo.getImagePath());
        String currentDate = DateUtil.format(new Date(), "yyyyMMdd");
        String folderPath = "/home/pat_saas/Upload/big/" + currentDate + "/";
        checkDirectory(folderPath);
        cutVo.setOutPath(folderPath);

        List<SpecialAnnDataVO> annoData = new ArrayList<>();
        for (SpecialAnnotation anno : annoList) {
            String location = anno.getGeometry();
            if (StringUtils.isEmpty(location)) {
                location = WktUtil.wktToJson(anno.getLocation());
            }
            JSONObject jsonObject = JSONObject.parseObject(location);
            if (null != jsonObject) {
                SpecialAnnDataVO vo = new SpecialAnnDataVO();
                String type = (String) jsonObject.get("type");
                List coordinates = (List) jsonObject.get("coordinates");
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(coordinates)) {
                    coordinates = getCoordinates(coordinates);
                }
                vo.setAnnotationId(anno.getSliceAnnotationId());
                vo.setType(type);
                vo.setCoordinates(coordinates);
                Integer categoryId = anno.getCategoryId();
                if (categoryId != null) {
                    //取缓存字典遍历id,匹配颜色
                    List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                        for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                            if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(categoryId))) {
                                String labelColor = dictData.getColor();
                                String labelName = dictData.getDictLabel();
                                vo.setCategoryName(labelName);
                                vo.setColor(labelColor);
                                break;
                            }
                        }
                    }
                }

                annoData.add(vo);
            }
        }

        cutVo.setAnnoData(annoData);
        cutVo.setImageName(imageName);
        String cutStr = JSONUtil.toJsonStr(cutVo);
        log.info("切片测试数据是：" + cutStr);
    }

    /**
     * 检查是否有文件夹，没有则创建
     *
     * @param folderPath
     */
    public void checkDirectory(String folderPath) {
        // 没有文件夹则创建新文件夹
        File file = new File(folderPath);
        if (!file.getParentFile().exists()) {
            log.info("文件目录1:" + folderPath + " 不存在，需要创建！");
            file.getParentFile().mkdirs();
        } else {
            log.info("文件目录1:" + folderPath + " 存在！");
        }

        if (!file.exists()) {
            log.info("文件目录2:" + folderPath + " 不存在，需要创建！");
            file.mkdirs();
        } else {
            log.info("文件目录2:" + folderPath + " 存在！");
        }
    }

    public void fillData(SpecialImage record, List<SpecialAnnotation> annoList) {
        Image hisImage = imageMapper.selectById(record.getImageId());
        //添加假数据
        int index = 1;
        for (SpecialAnnotation anno : annoList) {
            SubImage image = new SubImage();
            image.setSpecialId(record.getSpecialId());
            image.setSpecialAnnotationId(anno.getSliceAnnotationId());
            String imageName = hisImage.getImageName();
            String format = imageName;
            imageName = imageName.substring(0, imageName.lastIndexOf('.')) + "-" + index;
            String formatType = format.substring(format.lastIndexOf('.'), format.length());
            image.setParentImageCode(hisImage.getImageCode());
            image.setParentImageId(hisImage.getImageId());
            image.setParentImageName(hisImage.getImageName());
            image.setImageCode(IdUtils.randomUUID());
            image.setSliceBatchNumber(record.getSliceBatchNumber().longValue());
            image.setImageName(imageName + formatType);
            image.setImagePath("");
            image.setImageUrl("");
            image.setThumbUrl("");
            image.setVisceraType(anno.getCategoryId());
            image.setCreateTime(new Date());
            image.setSpecialImageId(record.getSpecialImageId());
            subImageService.save(image);
            index++;
        }


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

        if (auditStatus == 2) {
            paramMap.put("auditSucess", auditStatus);
        } else if (auditStatus == 1) {
            paramMap.put("auditSucess", auditStatus);
        }
        paramMap.put("specialImageIds", imageIds);
        paramMap.put("sliceImageStatus", 2);
        paramMap.put("specialId", vo.getSpecialId());
        List<SpecialImage> list = specialImageMapper.selectSpecialImageListByParm(paramMap);
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.size() != imageIds.length) {
                if (auditStatus == 1) {
                    return R.fail(MessageSource.M("PASS_ERROR"));
                } else {
                    return R.fail(MessageSource.M("NO_PASS_ERROR"));
                }
            }
        } else {
            return R.fail(MessageSource.M("DATA_NULL"));
        }


        //针对审核通过的数据，需要根据主图imageid、专题id、批次id查询对应的tb_sub_image所有小的切图，修改审核状态为通过
        List<Long> allSubIds = new ArrayList<>();
        for (SpecialImage sImage : list) {
            //修改specialImage审核状态
            SpecialImage record = new SpecialImage();
            BeanUtils.copyProperties(vo, record);
            record.setSpecialImageIds(null);
            record.setAuditTime(DateUtil.date());
            record.setSpecialImageId(sImage.getSpecialImageId());
            //审核状态 2：审核不通过
            if (auditStatus == 2) {
                //切图状态改为未切图 record.setEditBy(-1l);
                record.setSliceImageStatus(0);
                //这里是修改为空，恢复初始值
                record.setEditBy(-1L);
                //更新当前批次号加1
                record.setSliceBatchNumber(sImage.getSliceBatchNumber() + 1);
            }
            record.setUpdateTime(DateUtil.date());
            record.setUpdateBy(SecurityUtils.getUserId());
            //主图修改状态
            specialImageMapper.updateByPrimaryKeySelective(record);


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
            //审核状态 1：审核通过
            if (auditStatus == 1) {
                if (CollectionUtils.isNotEmpty(allSubIds)) {
                    //批量修改小图状态
                    SubImageVO simage = new SubImageVO();
                    simage.setImageIds(allSubIds);
                    simage.setAuditStatus(auditStatus);
                    simage.setUpdateTime(DateUtil.date());
                    simage.setUpdateBy(SecurityUtils.getUserId());
                    subImageMapper.updateByPrimaryKeySelective(simage);
                }
            }
        }
        return R.ok(null, MessageSource.M("OPERATE_ERROR"));
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
            //修改当前专题交付状态为已交付
            Special special = new Special();
            special.setSpecialId(vo.getSpecialId());
            //交付状态 0：未交付 1：已交付
            special.setDeliveryStatus(1L);
            special.setUpdateBy(SecurityUtils.getUserId());
            specialService.updateDeliveryStatus(special);
        } else {
            return R.fail(MessageSource.M("DELIVERY_FAIL"));
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

    /**
     * @param @param  specialImageSelectVO
     * @param @return
     * @return List<WaitSpecialImageVO>
     * @throws
     * @Title: selectWaitSpecialImage
     * @Description: 查询全部专题选片（根据切片编号/添加时间）
     */
    @Override
    public List<SpecialImageVO> selectSpecialImageList(SpecialImageSelectVO specialImageSelectVO) {
        return specialImageMapper.selectSpecialImageList(specialImageSelectVO);
    }

    /**
     * 切图测试
     *
     * @return
     */
    private String cutDemoData() {
        List<List<List<Integer>>> all_1 = new ArrayList<>();
        List<List<Integer>> p_1 = new ArrayList<>();

        String duo1 = "33384,9349]31786,8816]32505, 7112]27124, 9562]25606, 11747]22703, 11134]17055, 14703]13113, 14970]13113, 14970]11648, 12199]8771, 11853]";
        duo1 = duo1.replace(" ", "");
        String[] array1 = duo1.split("]");
        for (int i = 0; i < array1.length; i++) {
            String pv = array1[i];
            String[] pvArray = pv.split(",");
            String pv_1 = pvArray[0];
            String pv_2 = pvArray[1];

            List<Integer> perList_1 = new ArrayList<>();
            perList_1.add(Integer.valueOf(pv_1));
            perList_1.add(Integer.valueOf(pv_2));
            p_1.add(perList_1);
        }
        all_1.add(p_1);

        List<List<Integer>> p_2 = new ArrayList<>();

        String duo2 = "33384, 9349]31786, 8816]32505, 7112]27124, 9562]25606, 11747]22703, 11134]17055, 14703]13113, 14970]11648, 12199]8771, 11853]";
        duo2 = duo2.replace(" ", "");
        String[] array2 = duo2.split("]");
        for (int i = 0; i < array2.length; i++) {
            String pv = array1[i];
            String[] pvArray = pv.split(",");
            String pv_1 = pvArray[0];
            String pv_2 = pvArray[1];
            List<Integer> perList_1 = new ArrayList<>();
            perList_1.add(Integer.valueOf(pv_1));
            perList_1.add(Integer.valueOf(pv_2));
            p_2.add(perList_1);
        }
        all_1.add(p_2);

        Map<String, Object> o1 = new HashMap<>(16);
        o1.put("_roi_polygon", all_1);
        o1.put("file_path", "");
        o1.put("out_path", "/home/pat_saas/Upload/special/big/");
        String cd = JSONUtil.toJsonStr(o1);
        return cd;
    }

    @Override
    public SpecialImage selectByPrimaryKey(Long specialImageId) {
        return specialImageMapper.selectByPrimaryKey(specialImageId);
    }

    @Override
    public int updateByPrimaryKeySelective(SpecialImage record) {
        return specialImageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Image> getImageBySpecialId(SpecialImageSelectVO specialImageSelectVO) {
        List<Image> list = specialImageMapper.getImageBySpecialId(specialImageSelectVO);
        return list;
    }
}
