package cn.staitech.anno.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.staitech.anno.constant.SpecialImageConstant;
import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.document.GeometryDoc;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.specialAnnotation.SpecialAnnotation;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageInfoVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageSelectVO;
import cn.staitech.anno.domain.vo.specialImage.SpecialImageVO;
import cn.staitech.anno.domain.vo.specialImageAnno.*;
import cn.staitech.anno.domain.vo.specialImageAnno.in.SpecialAnnAddIn;
import cn.staitech.anno.domain.vo.specialImageAnno.in.SpecialAnnUpdateIn;
import cn.staitech.anno.enums.SysDictTypeEnum;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.service.GetUserInformationService;
import cn.staitech.anno.service.SpecialImageAnnoService;
import cn.staitech.anno.service.SpecialImageService;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.utils.CustomizationIdUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.SendMessage;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.staitech.anno.constant.AnnotationConstant.*;


/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: AnnotationController
 * @Description:专题选片标注
 * @date 2023年6月9日
 */
@Slf4j
@Api(value = "专题标注接口", tags = "专题标注接口")
@RestController
@Validated
@RequestMapping("/specialAnnotation")
public class SpecialAnnotationController {

    @Resource
    private SpecialImageService specialImageService;

    @Resource
    private GetUserInformationService getUserInformationService;

    @Resource
    private SpecialImageAnnoService specialImageAnnoService;

    @Resource
    private SubImageService subImageService;


    /**
     * @param @param  specialImageId
     * @param @param  queryType
     * @param @return
     * @return R
     * @throws
     * @Title: info
     * @Description: 查询标注列表
     */
    @SuppressWarnings("rawtypes")
    // @RequiresPermissions("anno:annotation:list")
    @ApiOperation(value = "查询标注列表")
    @Log(title = "查询标注列表", menu = "专题管理", subMenu = "查询标注列表", businessType = BusinessType.OTHER)
    @GetMapping("/info")
    public R info(
            @RequestParam @ApiParam(name = "specialImageId", value = "切片id", required = true) String specialImageId,
            @RequestParam @ApiParam(name = "type", value = "请求类型", required = true) int type) {
        //queryType 1：普通查看  2：编辑
        SpecialImageSelectVO specialImageSelectVO = new SpecialImageSelectVO();
        specialImageSelectVO.setSpecialImageId(Long.valueOf(specialImageId));
        List<SpecialImageVO> sivList = specialImageService.selectSpecialImageList(specialImageSelectVO);
        // 校验当前用户是否是编辑用户，确保数据只可以被同一个人编辑
        if (CollectionUtil.isEmpty(sivList)) {
            return R.fail(SpecialImageConstant.MARK_RELEVANCY_IMAGE);
        }
        SpecialImageVO sImage = sivList.get(0);
        //查看当前数据是否可以被编辑
        if (type == 2) {
            //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中,确保在绘制中可以进行修改，且提交人是绘制
            if (sImage.getSliceImageStatus() != 0) {
                if (sImage.getSliceImageStatus() == 3) {
                    if (sImage.getEditBy().longValue() == SecurityUtils.getUserId().longValue()) {
                        //当前编辑人可以继续操作
                    } else {
                        //其它人不可以编辑
                        return R.fail(SpecialImageConstant.NOT_PERMISSION);
                    }
                } else {
                    //其它人不可以编辑
                    return R.fail(SpecialImageConstant.NOT_PERMISSION);
                }
            }
        }
        List<SubImage> selectSubImageList = new ArrayList<>();
        //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中 4:切图失败
        //审核状态 0：待审核 1：审核通过 2：审核不通过
        if (sImage.getAuditStatus() == 1 || sImage.getAuditStatus() == 0) {
            //查询所有的subImage
            SubImage subImage = new SubImage();
            subImage.setSpecialId(sImage.getSpecialId());
            subImage.setParentImageId(sImage.getImageId());
            subImage.setSliceBatchNumber(sImage.getSliceBatchNumber().longValue());
            selectSubImageList = subImageService.selectSubImageList(subImage);
        }
        SpecialImageInfoVO info = new SpecialImageInfoVO();
        BeanUtils.copyProperties(sImage, info);
        info.setSubImageList(selectSubImageList);
        return R.ok(info);
    }


    /**
     * 编辑
     *
     * @param @param  specialImageId
     * @param @return
     * @param @throws Exception
     * @return R<String>
     * @throws
     * @Title: stopEdit
     * @Description: 结束编辑
     */
    @GetMapping("editSliceStatus")
    @ApiOperation(value = "结束编辑接口")
    //    @RequiresSpecialPermissions("specialConfig:slice:edit")
    @Log(title = "结束编辑", menu = "专题管理", subMenu = "结束编辑", businessType = BusinessType.UPDATE)
    public R<String> editSliceStatus(@RequestParam @ApiParam(name = "specialImageId", value = "切片id", required = true) Long specialImageId,
                                     @RequestParam @ApiParam(name = "sliceStatus", value = "切图状态", required = true) int sliceStatus) throws Exception {

        // 校验当前用户是否是编辑用户，确保数据只可以被同一个人编辑
        SpecialImage sImage = specialImageService.selectByPrimaryKey(specialImageId);
        if (null == sImage) {
            return R.fail(SpecialImageConstant.Data_NULL);
        }
        if (sliceStatus == 0 || sliceStatus == 3) {
            //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中,确保在绘制中可以进行修改，且提交人是绘制
            //操作权限控制
        } else {
            return R.fail(SpecialImageConstant.NOT_PERMISSION);
        }

        //判断当前编辑人员和edit_by是同一个
        //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中,确保在绘制中可以进行修改，且提交人是绘制
        if (sImage.getSliceImageStatus() == 3) {
            if (sImage.getEditBy().longValue() == SecurityUtils.getUserId().longValue()) {
                //当前编辑人可以继续操作
            } else {
                //其它人不可以编辑
                log.info("当前操作人id:" + SecurityUtils.getUserId().longValue() + " 数据编辑人id:" + sImage.getEditBy().longValue());
                return R.fail(SpecialImageConstant.NOT_PERMISSION);
            }
        } else {
            //其它人不可以编辑
            //			return R.fail(SpecialImageConstant.NOT_PERMISSION);
        }

        // remove source annotation 根据 标注id 删除标注信息
        SpecialImage record = new SpecialImage();
        record.setSpecialImageId(specialImageId);
        record.setSliceImageStatus(sliceStatus);
        if (sliceStatus == 3) {
            record.setEditBy(SecurityUtils.getUserId());
            //保存当前人的token
            //        	SysUser loginUser = getUserInformationService.selectById(SecurityUtils.getUserId());
            //根据用户id获取token，校验是否过期
            //			String userNameKey = CacheConstants.LOGIN_TOKEN_KEY+loginUser.getUserName();
            //			String cacheObject = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY+userNameKey);
            //			log.info("绘制中4："+SecurityUtils.getUsername()+" token:"+SecurityUtils.getToken());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(SecurityUtils.getToken())) {
                record.setUpdateByToken(SecurityUtils.getToken());
            }
        } else {
            record.setEditBy(-1L);
        }
        int deleteFlag = specialImageService.updateByPrimaryKeySelective(record);
        if (deleteFlag <= 0) {
            return R.fail(MessageSource.M("OPERATE_ERROR"));
        }
        return R.ok(null,MessageSource.M("OPERATE_SUCCEED"));
    }


    /**
     * @param @param  specialImageId
     * @param @return
     * @return R
     * @throws
     * @Title: subImageDetail
     * @Description: 查询切分明细
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "查询切分明细")
    @Log(title = "查询切分明细", menu = "专题管理", subMenu = "查询切分明细", businessType = BusinessType.OTHER)
    @GetMapping("/subImageDetail")
    public R subImageDetail(
            @RequestParam @ApiParam(name = "specialImageId", value = "切片id", required = true) Long specialImageId) {
        SpecialImage sImage = specialImageService.selectByPrimaryKey(specialImageId);
        if (null == sImage) {
            return R.fail(SpecialImageConstant.Data_NULL);
        }
        // 查询所有标注
        SpecialAnnotation annotation = new SpecialAnnotation();
        annotation.setSpecialImageId(specialImageId);

        //查询所有的subImage
        SubImage subImage = new SubImage();
        subImage.setSpecialId(sImage.getSpecialId());
        subImage.setParentImageId(sImage.getImageId());
        subImage.setSliceBatchNumber(sImage.getSliceBatchNumber().longValue());
        List<SubImage> selectSubImageLIst = subImageService.selectSubImageList(subImage);
        return R.ok(selectSubImageLIst);
    }


    /**
     * 查看
     *
     * @param specialImageId
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "查询切分明细")
    //    @RequiresSpecialPermissions("specialConfig:slice:view")
    @Log(title = "查询切分明细", menu = "专题管理", subMenu = "查询切分明细", businessType = BusinessType.OTHER)
    @GetMapping("/annDetail")
    public R annDetail(
            @RequestParam @ApiParam(name = "specialImageId", value = "切片id", required = true) Long specialImageId) {
        SpecialImage sImage = specialImageService.selectByPrimaryKey(specialImageId);
        if (null == sImage) {
            return R.fail(SpecialImageConstant.Data_NULL);
        }
        // 查询所有标注
        SpecialAnnotation annotation = new SpecialAnnotation();
        annotation.setSpecialImageId(specialImageId);
        List<SpecialAnnotation> annoList = specialImageAnnoService.selectSpecialAnnotationList(annotation);

        List<SpecialAnnotationsDetailVO> list = new ArrayList<>();
        for (SpecialAnnotation anno : annoList) {
            SpecialAnnotationsDetailVO annotationsDetailVO = new SpecialAnnotationsDetailVO();
            BeanUtils.copyProperties(anno, annotationsDetailVO);
            // 获取createUser对应用户信息
            if (null != anno.getCreateBy()) {
                annotationsDetailVO.setCreateName(getUserInformationService.selectById(anno.getCreateBy()).getUserName());
            }

            // 获取updateUser对应用户信息
            if (null != anno.getUpdateBy()) {
                annotationsDetailVO.setUpdateName(getUserInformationService.selectById(anno.getUpdateBy()).getUserName());
            }
            long categoryId = anno.getCategoryId();
            //取缓存字典遍历id,匹配颜色
            List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                    if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(categoryId))) {
                        annotationsDetailVO.setColor(dictData.getColor());
                    }
                }
            }
            list.add(annotationsDetailVO);
        }

        return R.ok(list);
    }


    /**
     * @param @param  specialImageId
     * @param @return
     * @param @throws Exception
     * @return R<String>
     * @throws
     * @Title: cutImage
     * @Description: 切图
     */
    @GetMapping("cutImage")
    @ApiOperation(value = "切图接口")
    @Log(title = "切图", menu = "专题管理", subMenu = "切图", businessType = BusinessType.INSERT)
    public R<String> cutImage(@RequestParam @ApiParam(name = "specialImageId", value = "切片id", required = true) String specialImageId) {
        // 校验当前用户是否是编辑用户，确保数据只可以被同一个人编辑
        SpecialImage sImage = specialImageService.selectByPrimaryKey(Long.valueOf(specialImageId));
        if (null == sImage) {
            return R.fail(SpecialImageConstant.Data_NULL);
        }
        //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
        if (sImage.getSliceImageStatus() == 1) {
            return R.fail(SpecialImageConstant.DELIVERY_ING);
        }
        if (sImage.getSliceImageStatus() == 2) {
            return R.fail(SpecialImageConstant.DELIVERY_AGAIN);
        }

        // 查询所有标注
        SpecialAnnotation annotation = new SpecialAnnotation();
        annotation.setSpecialImageId(Long.valueOf(specialImageId));
        List<SpecialAnnotation> annoList = specialImageAnnoService.selectSpecialAnnotationList(annotation);

        if (CollectionUtil.isEmpty(annoList)) {
            return R.fail(SpecialImageConstant.Data_NULL);
        }
        // 切图通知操作 1、查询所有标注结果列表+主图信息
        SpecialCutImageVO resData = new SpecialCutImageVO();
        resData.setAnnoList(annoList);
        resData.setImage(sImage);
        specialImageService.cutImageNotice(resData);

        return R.ok(null,MessageSource.M("OPERATE_SUCCEED"));
    }

    @PostMapping("callBackCutImage")
    @ApiOperation(value = "切图回调接口")
    //	@RequiresPermissions("anno:annotation:remove")
    @Log(title = "切片配置", menu = "专题管理", subMenu = "切图回调接口", businessType = BusinessType.OTHER)
    //	public R<String> callBackCutImage(@RequestParam(name = "specialImageId", value = "切片id", required = false) Long specialImageId){
    public R<String> callBackCutImage() {
        //		SpecialImage sImage = specialImageService.selectByPrimaryKey(specialImageId);
        //数据校验
        //重复性校验
        //保存处理
        // 校验当前用户是否是编辑用户，确保数据只可以被同一个人编辑
		/*SpecialImage sImage = specialImageService.selectByPrimaryKey(specialImageId);
		if (null == sImage) {
			return R.fail(SpecialImageConstant.Data_NULL);
		}


		List<SubImage> list = new ArrayList<>();
		subImageService.saveBatch(list);


		// 查询所有标注
		SpecialAnnotation annotation = new SpecialAnnotation();
		annotation.setSpecialImageId(specialImageId);
		List<SpecialAnnotation> annoList = specialImageAnnoService.selectSpecialAnnotationList(annotation);

		if(CollectionUtil.isEmpty(annoList)){
			return R.fail(SpecialImageConstant.Data_NULL);
		}
		// 切图通知操作 1、查询所有标注结果列表+主图信息
		// 加入 队列 resData
		SpecialCutImageVO  resData = new SpecialCutImageVO();
		resData.setAnnoList(annoList);
		resData.setImage(sImage);
		//切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中,确保在绘制中可以进行修改，且提交人是绘制
		if(sImage.getSliceImageStatus() == 3 ){
			//修改为生成中
			SpecialImage record = new SpecialImage();
			record.setSpecialImageId(specialImageId);
			record.setEditBy(SecurityUtils.getUserId());
			record.setSliceImageStatus(1);
			specialImageService.updateByPrimaryKeySelective(record);
		}*/
        return R.ok(null,MessageSource.M("OPERATE_SUCCEED"));
    }


    //	@RequiresPermissions("anno:annotation:add")
    @SuppressWarnings("unused")
    @ApiOperation(value = "标注结果接口（批量操作）")
    @Log(title = "切片配置-标注结果接口（批量操作）", menu = "专题管理", subMenu = "切片配置-标注结果接口（批量操作）", businessType = BusinessType.OTHER)
    @PostMapping("/annotationSave")
    public R<List<SpecialAnnoAddVO>> annotationSave(@Validated @RequestBody List<SpecialAnnoAddVO> annoList) throws Exception {
        SpecialImage sImage = new SpecialImage();
        if (CollectionUtil.isNotEmpty(annoList)) {
            // 校验当前用户是否是编辑用户，确保数据只可以被同一个人编辑
            sImage = specialImageService.selectByPrimaryKey(annoList.get(0).getSpecialImageId());
            //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中
            if (sImage.getSliceImageStatus() == 1 || sImage.getSliceImageStatus() == 2) {
                return R.fail(SpecialImageConstant.ANNO_NO);
            }
            if (null == sImage) {
                return R.fail(SpecialImageConstant.Data_NULL);
            }
            //判断当前编辑人员和edit_by是同一个
            //切图状态 0:未切图 1：生成中 2：切图完成 3：绘制中,确保在绘制中可以进行修改，且提交人是绘制
            if (sImage.getEditBy().longValue() == SecurityUtils.getUserId().longValue()) {
                //当前编辑人可以继续操作
            } else {
                //其它人不可以编辑
//				return R.fail(SpecialImageConstant.NOT_PERMISSION);
            }
            if (sImage.getSliceImageStatus() == 1 || sImage.getSliceImageStatus() == 2) {
                //其它人不可以编辑
//				return R.fail(SpecialImageConstant.NOT_PERMISSION);
            }

        }
        R<List<SpecialAnnoAddVO>> retStr = specialImageAnnoService.annotationSave(annoList, sImage);
        return retStr;
    }

    /**
     * @param @param  list
     * @param @param  clazz
     * @param @return
     * @return List<T>
     * @throws
     * @Title: copy
     * @Description: 列表复制
     */
    public static <T> List<T> copy(List<?> list, Class<T> clazz) {
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }


    @ApiOperation(value = "添加标注")
    @Log(title = "切片配置-保存标注结果", menu = "专题管理", subMenu = "切片配置-保存标注结果", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<Long> add(@Validated @RequestBody SpecialAnnAddIn req) throws Exception {
//       log.info("add-info-1:"+JSONUtil.toJsonStr(req));
        // 查询切片表中切片信息
        SpecialImage specialImage = specialImageService.selectByPrimaryKey(req.getSlide_id());
        if (specialImage == null) {
            return R.fail("未查询到切片信息");
        }
        SpecialAnnotation specialAnnotation = new SpecialAnnotation();
        BeanUtils.copyProperties(req, specialAnnotation);
        specialAnnotation = getTransBySpecialAnnAddIn(req);
        specialAnnotation.setCreateBy(SecurityUtils.getUserId());
        specialAnnotation.setCreateTime(DateUtil.date());
        // 拼接标注名称
        if (StringUtils.isEmpty(req.getMeasure_name())) {
            req.setMeasure_name("");
        }
        String numKey = req.getSlide_id() + "_" + req.getMeasure_name();
        //		Long numId = redisClientUtil.getAndAddLong("labelNameNum:" + numKey, 1L);
        // 获取标注名称 null278_null_大脑切面1
        String measure_full_name = req.getMeasure_name() + numKey + "_";
        String label_color = null;
        String label_name = null;
        if (req.getCategory_id() != null) {
            //取缓存字典遍历id,匹配颜色
            List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                    if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(req.getCategory_id()))) {
                        label_color = dictData.getColor();
                        label_name = dictData.getDictLabel();
                        measure_full_name += label_name;
                        break;
                    }
                }
            }
        }
        String annotationId = CustomizationIdUtils.getSdId();
        specialAnnotation.setAnnotationId(annotationId);
        specialAnnotation.setCreateBy(SecurityUtils.getUserId());
        specialAnnotation.setMeasureFullName(measure_full_name);
        specialAnnotation.setAnnotationType("Draw");
//		 log.info("add-info-2:"+JSONUtil.toJsonStr(specialAnnotation));
        // 添加数据库，添加后返回自增id
        specialImageAnnoService.insertSpecialAnnotation(specialAnnotation);

        if (Objects.equals(req.getLocation_type(), "Point")) {
            PointCount pointCounts = specialImageAnnoService.selectCategoryCount(specialAnnotation);
            specialAnnotation.setPointCount(pointCounts.getPoint_count().intValue());
            // 删除后更新标注点数据
            specialImageAnnoService.updatePointCount(specialAnnotation);
        }
        // 写入文件中
        // 获取geojsonUrl地址
        //        String geojsonUrl = specialImageAnnoService.getGeojsonUrls(specialImage.getGeojsonUrl(), specialImage.getSpecialImageId());
        //        specialImage.setGeojsonUrl(geojsonUrl);
        // 更新slide表中geojsonUrl地址
        //        slideService.updateById(slide);
        // 构建完整信息
        SpecialAnnotation specialAnn = specialImageAnnoService.selectByPrimaryKey(specialAnnotation.getSliceAnnotationId());
        AnnoProperties properties = new AnnoProperties();
//		Properties properties = new Properties();
//		BeanUtils.copyProperties(specialAnn, properties);
        properties = specialImageAnnoService.getPropertiesBy(specialAnn);
//		properties.setMeasure_full_name(measure_full_name);
        properties.setLabel_color(label_color);
        properties.setLabel_name(label_name);
        properties.setCreate_time(DateUtil.format(specialAnn.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        properties.setAnnotation_owner(SecurityUtils.getUsername());
        properties.setMarking_id(specialAnn.getSliceAnnotationId());

        // 将geo写入es中
        GeometryDoc geometryDoc = new GeometryDoc();
        geometryDoc.setMarking_id(specialAnnotation.getSliceAnnotationId());
        String jsonStr = req.getGeometry().toString();
        geometryDoc.setGeometry(jsonStr);
        geometryDoc.setSlideId(req.getSlide_id());
        geometryDoc.setCreate_time(DateUtil.format(specialAnnotation.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        BeanUtils.copyProperties(properties, geometryDoc);
        //        geometryDocMapper.save(geometryDoc);
        //发送websocket
//		Features features = new Features();
        AnnoFeatures features = new AnnoFeatures();
        if (StringUtils.isNotEmpty(specialAnnotation.getGeometry())) {
            JSONObject geometryJsonObject = JSONObject.parseObject(specialAnnotation.getGeometry());
            features.setGeometry(geometryJsonObject);
        } else {
            JSONObject geometryJsonObject = new JSONObject();
            features.setGeometry(geometryJsonObject);
        }

//		features.setGeometry(req.getGeometry());
        features.setId(annotationId);
        features.setType("Feature");
        features.setProperties(properties);

        List<PointCount> pointCountList = new ArrayList<>();
        // 判断是否需要发送点总数
        if (Objects.equals(req.getLocation_type(), "Point")) {
            // 查询总点数
            PointCount pointCounts = specialImageAnnoService.selectCategoryCount(specialAnn);
            specialAnnotation.setPointCount(pointCounts.getPoint_count().intValue());
            // 添加后更新标签点数
            specialImageAnnoService.updatePointCount(specialAnnotation);
            // 查询点总数

            PointCount pointCount = specialImageAnnoService.selectCategoryCount(specialAnn);
            pointCountList.add(pointCount);
        }

        // 写入文件
        //        FileUtils.addGeojson(features, geojsonUrl, req.getSlide_id());
        // 更新点数量
        //        updatePointCount(features, slide.getGeojsonUrl());
        AnnoBroadcastVO broadcastVO = SendMessage.sendAnnoMessages(ADD_STATUS, features, pointCountList);
        NioWebSocketHandler.sendAnnoAll(req.getSlide_id(), broadcastVO);
        return R.ok(specialAnn.getSliceAnnotationId(), MessageSource.M("OPERATE_SUCCEED"));
    }


    /**
     * 更新viewer页面上数据
     *
     * @param req 传入参数
     * @return String
     */
    @ApiOperation(value = "更新标注")
    @Log(title = "修改标注", menu = "专题管理", subMenu = "修改标注", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Long> update(@Validated @RequestBody SpecialAnnUpdateIn req) throws Exception {

        // 查询标注表中信息
        SpecialAnnotation markingBy = specialImageAnnoService.selectByPrimaryKey(req.getMarking_id());
        if (!Optional.ofNullable(markingBy).isPresent()) {
            return R.fail("未查询到标注信息");
        }
        // 查询标注表中信息
		/*Slide slide = slideService.getById(markingBy.getSlide_id());
        if (!Optional.ofNullable(slide).isPresent()) {
            return R.fail("未查询到切片信息");
        }
        if (!Optional.ofNullable(slide.getGeojsonUrl()).isPresent()) {
            return R.fail("未查询到标注信息");
        }*/
        // 更新前数据
//		PointCount pointCount = specialImageAnnoService.selectCategoryCount(markingBy);
        // 更新文件中的内容
        String measureFullName = markingBy.getMeasureFullName();
        SpecialAnnotation anning = new SpecialAnnotation();
        BeanUtils.copyProperties(req, anning);
        anning = getTransBySpecialAnnUpdateIn(req);
        anning.setSliceAnnotationId(req.getMarking_id());
        String label_color = null;
        String label_name = null;
        if (req.getCategory_id() != null) {
            //取缓存字典遍历id,匹配颜色
            List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                    if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(req.getCategory_id()))) {
                        label_color = dictData.getColor();
                        label_name = dictData.getDictLabel();
                        String res1 = String.valueOf(measureFullName.charAt(measureFullName.length() - 1));
                        if (res1.equals("_")) {
                            measureFullName = measureFullName + label_name;
                        } else {
                            measureFullName = measureFullName.replaceAll(measureFullName.split("_")[measureFullName.split("_").length - 1], label_name);
                        }
                    }
                }
            }
        }
        anning.setMeasureFullName(measureFullName);
        anning.setUpdateBy(SecurityUtils.getUserId());
        anning.setUpdateTime(DateUtil.date());
        specialImageAnnoService.update(anning);

        SpecialAnnotation markingBys = specialImageAnnoService.selectByPrimaryKey(req.getMarking_id());
        // 更新更改前的标注点数
        // 查询点数量
        // 查询旧标签id
        List<PointCount> pointCountList = new ArrayList<>();
        if (Objects.equals(markingBys.getLocationType(), "Point")) {
            if (!Objects.equals(req.getCategory_id(), markingBy.getCategoryId())) {
                PointCount pointCount1 = specialImageAnnoService.selectCategoryCount(markingBys);
                markingBys.setPointCount(pointCount1.getPoint_count().intValue());
                specialImageAnnoService.updatePointCount(markingBys);
                pointCountList.add(pointCount1);
            }
            PointCount newPointCount = specialImageAnnoService.selectCategoryCount(markingBy);
            newPointCount.setCategory_id(markingBy.getCategoryId().longValue());
            markingBy.setPointCount(newPointCount.getPoint_count().intValue());
            specialImageAnnoService.updatePointCount(markingBy);
            pointCountList.add(newPointCount);
        }
        // 查询更新后的标注信息
        // 更新到es中
        GeometryDoc geometryDoc = new GeometryDoc();
        BeanUtils.copyProperties(markingBys, geometryDoc);
        // 更新es中数据
        geometryDoc.setMarking_id(anning.getSliceAnnotationId());
        if (req.getGeometry() != null) {
            String jsonStr = req.getGeometry().toString();
            geometryDoc.setGeometry(jsonStr);
        }
        //        geometryDocMapper.save(geometryDoc);
        //发送websocket

        SpecialAnnotation markingBy2 = specialImageAnnoService.selectByPrimaryKey(req.getMarking_id());
//		Properties properties = new Properties();
//		BeanUtils.copyProperties(markingBy2, properties);
        AnnoProperties properties = new AnnoProperties();
        properties = specialImageAnnoService.getPropertiesBy(markingBy2);
        properties.setLabel_color(label_color);
        properties.setLabel_name(label_name);
//		properties.setMeasure_full_name(measureFullName);
        properties.setAnnotation_type(markingBy.getAnnotationType());
        properties.setAnnotation_owner(SecurityUtils.getUsername());
        properties.setCreate_time(DateUtil.format(markingBy.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
//		Features features = new Features();
        AnnoFeatures features = new AnnoFeatures();
        if (StringUtils.isNoneEmpty(markingBy2.getGeometry())) {
            JSONObject geometryJsonObject = JSONObject.parseObject(markingBy2.getGeometry());
            features.setGeometry(geometryJsonObject);
        } else {
            JSONObject geometryJsonObject = new JSONObject();
            features.setGeometry(geometryJsonObject);
        }
        features.setId(markingBy.getAnnotationId());
        features.setType("Feature");
        features.setProperties(properties);


        // 查找点类型
        //        GeoMarking markingBy1 = viewerService.constructUpdMarking(req, marking1.getProperties().getMeasure_full_name());
        //        updateGeojson(features, slide.getGeojsonUrl());

		/*if (Objects.equals(markingBy.getLocationType(), "Point")) {
            // 更新前点数
            Features features1 = new Features();
            Properties properties1 = new Properties();
            properties1.setCategory_id(markingBy.getCategoryId().longValue());
            features1.setProperties(properties1);
            updatePointCount(features1, slide.getGeojsonUrl());
            // 更新后点数
            updatePointCount(features, slide.getGeojsonUrl());
        }*/
        AnnoBroadcastVO broadcastVO = SendMessage.sendAnnoMessages(UPDATE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAnnoAll(markingBy.getSpecialImageId(), broadcastVO);
        return R.ok(req.getMarking_id(), MessageSource.M("OPERATE_SUCCEED"));
    }


    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "marking_id", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @Log(title = "删除标注", menu = "专题管理", subMenu = "删除标注", businessType = BusinessType.DELETE)
    @PostMapping("/del")
    public R<String> del(Long marking_id) throws Exception {

        if (!Optional.ofNullable(marking_id).isPresent()) {
            return R.fail("参数异常");
        }
        // 查询标注表中信息
        SpecialAnnotation markingBy = specialImageAnnoService.selectByPrimaryKey(Long.valueOf(marking_id));

        if (!Optional.ofNullable(markingBy).isPresent()) {
            return R.fail("未查询到标注信息");
        }

        SpecialImage specialImage = specialImageService.selectByPrimaryKey(markingBy.getSpecialImageId());

        //        /Slide slide = slideService.getById(markingBy.getSlide_id());
        if (!Optional.ofNullable(specialImage).isPresent()) {
            return R.fail("未查询到切片信息");
        }
        // 删除前查询详情数据
        specialImageAnnoService.deleteAnnotationById(Long.valueOf(marking_id));
		/*Optional<GeometryDoc> search = geometryDocMapper.findById(String.valueOf(marking_id));
        GeometryDoc geometryDoc = search.orElse(null);
        String geometry = null;
        if (geometryDoc != null) {
            geometry = search.get().getGeometry();
        }
        com.alibaba.fastjson.JSONObject geometryJson = new com.alibaba.fastjson.JSONObject(Boolean.parseBoolean(geometry));
        // 删除es中标注数据
        geometryDocMapper.deleteById(String.valueOf(marking_id));*/
        // 删除后根据id查询标注详情
        String label_color = null;
        String label_name = null;
        if (markingBy.getCategoryId() != null) {
            //取缓存字典遍历id,匹配颜色
            List<cn.staitech.system.api.domain.SysDictData> sysDictList = cn.staitech.common.security.utils.DictUtils.getDictCache(SysDictTypeEnum.sysvisceraorganization.label());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysDictList)) {
                for (cn.staitech.system.api.domain.SysDictData dictData : sysDictList) {
                    if (dictData.getDictValue().equalsIgnoreCase(String.valueOf(markingBy.getCategoryId()))) {
                        label_color = dictData.getColor();
                        label_name = dictData.getDictLabel();
                        break;
                    }
                }
            }
        }
        //发送websocket
//		Properties properties = new Properties();
//		BeanUtils.copyProperties(markingBy, properties);
        AnnoProperties properties = new AnnoProperties();
        properties = specialImageAnnoService.getPropertiesBy(markingBy);
        properties.setLabel_color(label_color);
        properties.setLabel_name(label_name);
        //TODO 处理
//		Features features = new Features();
        AnnoFeatures features = new AnnoFeatures();
//		String location = WktUtil.wktToJson(markingBy.getLocation());
        JSONObject geometryJsonObject = JSONObject.parseObject(markingBy.getGeometry());
        //		Features featureLocation = new Features();
        //			featureLocation.setId(markingBy.getAnnotationId());
        //			featureLocation.setGeometry(geometryJsonObject);
        //			featureLocation.setProperties(properties);
        //			featureLocation.setType("Feature");

        features.setGeometry(geometryJsonObject);
        features.setId(String.valueOf(markingBy.getAnnotationId()));
        features.setType("Feature");
        features.setProperties(properties);

        List<PointCount> pointCountList = new ArrayList<>();
        // 判断是否为点类型
        if (Objects.equals(markingBy.getLocationType(), "Point")) {
            // 更新总点数
            PointCount pointCounts = specialImageAnnoService.selectCategoryCount(markingBy);
            markingBy.setPointCount(pointCounts.getPoint_count().intValue());
            // 删除后更新标注点数据
            specialImageAnnoService.updatePointCount(markingBy);
            // 查询点总数
            PointCount pointCount = specialImageAnnoService.selectCategoryCount(markingBy);
            pointCountList.add(pointCount);

//			Features features1 = new Features();
//			Properties properties1 = new Properties();
            AnnoProperties properties1 = new AnnoProperties();
            AnnoFeatures features1 = new AnnoFeatures();
            properties1.setCategory_id(markingBy.getCategoryId().longValue());
//			properties1.setPoint_count(pointCounts.getPoint_count());
            properties1.setLocation_type(markingBy.getLocationType());
            features1.setProperties(properties1);
            //            updatePointCount(features1, slide.getGeojsonUrl());
        }

        // 删除标注信息
		/*boolean res = delGeojson(marking_id, slide.getGeojsonUrl());
        if (!res) {
            return R.fail("删除标注失败");
        }*/
        AnnoBroadcastVO broadcastVO = SendMessage.sendAnnoMessages(DELETE_STATUS, features, pointCountList);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAnnoAll(markingBy.getSpecialImageId(), broadcastVO);
        // 发送websocket
        return R.ok(null,MessageSource.M("OPERATE_SUCCEED"));
    }


    @ApiOperation(value = "获取标注数据")
    @Log(title = "获取标注数据", menu = "专题管理", subMenu = "获取标注数据", businessType = BusinessType.QUERY)
    @GetMapping("/getAnnotation")
    public R<JSONObject> getAnnotation(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }

        SpecialImage specialImage = specialImageService.selectByPrimaryKey(slideId);
        if (specialImage == null) {
            return R.fail("未发现切片信息");
        }
        //TODO
        SpecialAnnotation annotation = new SpecialAnnotation();
        annotation.setSpecialImageId(slideId);
//		List<SpecialAnnoProperties> list = specialImageAnnoService.selectSpecialPropertiesList(annotation);
        List<SpecialAnnotation> list = specialImageAnnoService.selectSpecialAnnotationList(annotation);
        if (CollectionUtil.isNotEmpty(list)) {
            AnnoMarkGeojson markGeojson = specialImageAnnoService.getMarkGeojsonByList(list);
            JSONObject parse = new JSONObject();
            parse = (JSONObject) JSONObject.toJSON(markGeojson);
            return R.ok(parse);
        }
        return R.ok(new JSONObject());
    }


    public SpecialAnnotation getTransBySpecialAnnAddIn(SpecialAnnAddIn req) {
        SpecialAnnotation specialAnnotation = new SpecialAnnotation();
        if (null != req.getSlide_id()) {
            specialAnnotation.setSpecialImageId(req.getSlide_id());
        }
        if (null != req.getSpecial_id()) {
            specialAnnotation.setSpecialId(req.getSpecial_id());
        }

        if (null != req.getImage_id()) {
            specialAnnotation.setImageId(req.getImage_id());
        }

        if (null != req.getCategory_id()) {
            specialAnnotation.setCategoryId(req.getCategory_id().intValue());
        }
        if (StringUtils.isNotEmpty(req.getLocation_type())) {
            specialAnnotation.setLocationType(req.getLocation_type());
        }
        if (null != req.getMeasure_type()) {
            specialAnnotation.setMeasureType(req.getMeasure_type().intValue());
        }
        if (StringUtils.isNotEmpty(req.getMeasure_relation())) {
            specialAnnotation.setMeasureRelation(req.getMeasure_relation());
        }

        if (StringUtils.isNotEmpty(req.getMeasure_name())) {
            specialAnnotation.setMeasureName(req.getMeasure_name());
        }
        if (null != req.getMeasure_number()) {
            specialAnnotation.setMeasureNumber(req.getMeasure_number().intValue());
        }
        if (null != req.getMean_distance()) {
            specialAnnotation.setMeanDistance(Double.valueOf(req.getMean_distance()));
        }
        if (null != req.getMax_distance()) {
            specialAnnotation.setMaxDistance(Double.valueOf(req.getMax_distance()));
        }
        if (null != req.getMin_distance()) {
            specialAnnotation.setMinDistance(Double.valueOf(req.getMin_distance()));
        }
        if (StringUtils.isNotEmpty(req.getInner_angle())) {
            specialAnnotation.setInnerAngle(req.getInner_angle());
        }
        if (StringUtils.isNotEmpty(req.getExterior_angle())) {
            specialAnnotation.setExteriorAngle(req.getExterior_angle());
        }
        if (StringUtils.isNotEmpty(req.getCenter_point())) {
            specialAnnotation.setCenterPoint(req.getCenter_point());
        }

        if (null != req.getGeometry()) {
            specialAnnotation.setGeometry(req.getGeometry().toString());
        }

        if (StringUtils.isNotEmpty(req.getArea())) {
            specialAnnotation.setArea(req.getArea());
        }
        if (StringUtils.isNotEmpty(req.getPerimeter())) {
            specialAnnotation.setPerimeter(new BigDecimal(req.getPerimeter()));
        }
        return specialAnnotation;
    }


    public SpecialAnnotation getTransBySpecialAnnUpdateIn(SpecialAnnUpdateIn req) {
        SpecialAnnotation specialAnnotation = new SpecialAnnotation();

        if (null != req.getSpecial_id()) {
            specialAnnotation.setSpecialId(req.getSpecial_id());
        }

        if (null != req.getImage_id()) {
            specialAnnotation.setImageId(req.getImage_id());
        }

        if (null != req.getGeometry()) {
            specialAnnotation.setGeometry(req.getGeometry().toString());
        }


        if (null != req.getCategory_id()) {
            specialAnnotation.setCategoryId(req.getCategory_id().intValue());
        }
        if (StringUtils.isNotEmpty(req.getLocation_type())) {
            specialAnnotation.setLocationType(req.getLocation_type());
        }
        if (null != req.getMeasure_type()) {
            specialAnnotation.setMeasureType(req.getMeasure_type().intValue());
        }
        if (StringUtils.isNotEmpty(req.getMeasure_relation())) {
            specialAnnotation.setMeasureRelation(req.getMeasure_relation());
        }

        if (StringUtils.isNotEmpty(req.getMeasure_name())) {
            specialAnnotation.setMeasureName(req.getMeasure_name());
        }
        if (null != req.getMeasure_number()) {
            specialAnnotation.setMeasureNumber(req.getMeasure_number().intValue());
        }
        if (null != req.getMean_distance()) {
            specialAnnotation.setMeanDistance(Double.valueOf(req.getMean_distance()));
        }
        if (null != req.getMax_distance()) {
            specialAnnotation.setMaxDistance(Double.valueOf(req.getMax_distance()));
        }
        if (null != req.getMin_distance()) {
            specialAnnotation.setMinDistance(Double.valueOf(req.getMin_distance()));
        }
        if (StringUtils.isNotEmpty(req.getInner_angle())) {
            specialAnnotation.setInnerAngle(req.getInner_angle());
        }
        if (StringUtils.isNotEmpty(req.getExterior_angle())) {
            specialAnnotation.setExteriorAngle(req.getExterior_angle());
        }
        if (StringUtils.isNotEmpty(req.getCenter_point())) {
            specialAnnotation.setCenterPoint(req.getCenter_point());
        }

        if (StringUtils.isNotEmpty(req.getArea())) {
            specialAnnotation.setArea(req.getArea());
        }
        if (StringUtils.isNotEmpty(req.getPerimeter())) {
            specialAnnotation.setPerimeter(new BigDecimal(req.getPerimeter()));
        }
        return specialAnnotation;
    }


}