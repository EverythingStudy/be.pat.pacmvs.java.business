package cn.staitech.anno.controller;

import cn.staitech.anno.constant.ExaminationConstant;
import cn.staitech.anno.constant.R.ExaminationResponseConstant;
import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.vo.*;
import cn.staitech.anno.enums.ExaminationEnum;
import cn.staitech.anno.enums.ProcessFlagEnum;
import cn.staitech.anno.service.*;
import cn.staitech.anno.utils.ExaminationUtils;
import cn.staitech.anno.utils.JsonUtils;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.ProjectUtils;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import static cn.staitech.anno.constant.ExaminationConstant.SUBMIT_REVIEW;
import static cn.staitech.anno.constant.R.ExaminationResponseConstant.IMAGE_NOT_REVIEW;
import static cn.staitech.anno.constant.R.ExaminationResponseConstant.REVIEW_NOT_PASS;
import static cn.staitech.anno.constant.ImageConstant.OPERATE_SUCCEED;
import static cn.staitech.anno.enums.ExaminationEnum.STATUS_INFO_3;

/**
 * 复核图像 .
 * 2023-04-07
 *
 * @author 杨磊、赵孟杰、王峰
 */
@Slf4j
@Api(value = "复核管理接口", tags = "复核管理")
@RestController
@RequestMapping("/examination")
@RefreshScope
public class ExaminationController extends BaseController {

    @Resource
    private ExaminationService examinationService;

    @Resource
    private SlideService slideService;

    @Resource
    private AnnotationService annotationService;

    @Resource
    private ExaminationLogService examinationLogService;

    @Resource
    private GetUserInformationService getUserInformationService;

    @Value("${jsonFilePath}")
    private String path;

    /**
     * 切片提交复核
     *
     * @param examinationSubmitVo
     * @return
     */
    @ApiOperation(value = "切片提交复核接口")
    @RequiresPermissions("anno:examination:submit")
    @Log(title = "切片提交复核接口", businessType = BusinessType.UPDATE)
    @PostMapping("/submit")
    public R<String> submit(@Validated @RequestBody ExaminationSubmitVO examinationSubmitVo) {
        Long slideId = examinationSubmitVo.getSlideId();

        // 标注状态
        Integer processFlag = Integer.valueOf(slideService.selectById(slideId).getProcessFlag());
        ExaminationLog examinationLog = new ExaminationLog();
        if (processFlag == ProcessFlagEnum.STATUS_INFO_2.value()) {
            examinationSubmitVo.setExaminationFlag(ExaminationConstant.NOT_START_REVIEW);
            examinationSubmitVo.setProcessFlag(SUBMIT_REVIEW);
            examinationSubmitVo.setUpdateBy(SecurityUtils.getUserId());
            examinationService.updateExaminationSubmit(examinationSubmitVo);

            Slide slide = slideService.selectById(slideId);
            //更新项目时间
            ProjectUtils.updateProjectStatus(slide.getProjectId());
            //更新项目切片时间
            ProjectUtils.updateSlideTime(slideId);
            ExaminationUtils.examinationStateLog(examinationLog, slide, slide.getExaminationFlag());
        }
        return R.ok(null, ResponseConstant.OPERATE_SUCCEED);
    }

    /**
     * 查询复核图像列表
     *
     * @param examinationSelectVo
     * @return
     */
    @RequiresPermissions("anno:examination:list")
    @ApiOperation(value = "复核图像列表接口")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<ExaminationListVO>> list(ExaminationSelectVO examinationSelectVo) {
        startPage();
        List<ExaminationListVO> list = examinationService.selectExaminationList(examinationSelectVo);
        for (ExaminationListVO examinationListVo : list) {
            examinationListVo.setExaminationFlagName(
                    ExaminationEnum.getEnumlabelByValue(examinationListVo.getExaminationFlag()));
        }
        //分页
        PageMaster<ExaminationListVO> pageMaster = new PageMaster<>(list);
        return R.ok(pageMaster);
    }

    /**
     * 修改复核图像状态
     *
     * @param examinationStateVo
     * @return
     */
    @RequiresPermissions(value = {"anno:examination:edit", "anno:annotation:examination"}, logical = Logical.OR)
    @Log(title = "复核状态", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "复核图像状态操作接口")
    @PostMapping("/edit")
    public R<String> edit(@Validated @RequestBody ExaminationStateVO examinationStateVo) {
        // 获取切片ID
        Long slideId = examinationStateVo.getSlideId();
        // 切片信息
        Slide slide1 = slideService.selectById(slideId);
        // 当前标注状态
        Integer processFlag = Integer.valueOf(slide1.getProcessFlag());

        ExaminationLog examinationLog = new ExaminationLog();
        //  当前为已提交复核状态
        if (processFlag == (ProcessFlagEnum.STATUS_INFO_3.value())) {
            // 复核未通过
            if (examinationStateVo.getExaminationFlag() == (STATUS_INFO_3.value())) {
                examinationStateVo.setProcessFlag(1);
                examinationStateVo.setExaminationFlag(3);
                Slide slide = ExaminationUtils.getSlide(examinationStateVo, slideId);
                ExaminationUtils.examinationStateLog(examinationLog, slide, SUBMIT_REVIEW);
                return R.ok(null, REVIEW_NOT_PASS);
            } else {
                Slide slide = ExaminationUtils.getSlide(examinationStateVo, slideId);
                ExaminationUtils.examinationStateLog(examinationLog, slide, slide.getExaminationFlag());
                return R.ok(null, OPERATE_SUCCEED);
            }
        }
        return R.fail(null, IMAGE_NOT_REVIEW);
    }

    /**
     * 交付JSON生成
     *
     * @param slideId
     * @return
     * @throws IOException
     */
    @RequiresPermissions(value = {"anno:examination:deliver"})
    @ApiOperation(value = "交付JSON生成接口")
    @GetMapping("/deliver")
    public R<String> json(
            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId)
            throws IOException {
        List<AnnotationJsonVO> annotations = annotationService.selectAnnotationJson(slideId);
        Slide slide = slideService.selectById(slideId);
        List<AnnotationJsonVO> annotationList = new ArrayList<>();
        annotations.forEach(o -> {
            AnnotationJsonVO annotation = new AnnotationJsonVO();
            annotation.setAnnotationId(o.getAnnotationId());
            annotation.setCoordinates(o.getCoordinates());
            annotation.setColor(o.getColor());
            annotation.setCategoryName(o.getCategoryName());
            annotationList.add(annotation);
        });

        File directories = new File(path + File.separator + ExaminationConstant.FILE_PATH);
        File fileName = new File(slideId + ExaminationResponseConstant.FILE_SUFFIX);
        File file = new File(directories + File.separator + fileName);

        JsonUtils.createDirectories(directories);
        JsonUtils.createFile(annotationList, file, slide);
        return R.ok(fileName.toString(), ResponseConstant.OPERATE_SUCCEED);
    }


    /**
     * 交付JSON文件下载
     *
     * @param slideId
     * @param response
     */
    @RequiresPermissions(value = {"anno:examination:download"})
    @ApiOperation(value = "交付JSON文件下载接口")
    @PostMapping("/download")
    public void download(
            @RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId,
            HttpServletResponse response) {

        // 查询数据
        List<AnnotationJsonVO> annotations = annotationService.selectAnnotationJson(slideId);

        List<AnnotationJsonVO> annotationList = new ArrayList<>();
        annotations.forEach(o -> {
            AnnotationJsonVO annotation = new AnnotationJsonVO();
            annotation.setAnnotationId(o.getAnnotationId());
            annotation.setCoordinates(o.getCoordinates());
            annotation.setColor(o.getColor());
            annotation.setCategoryName(o.getCategoryName());
            annotationList.add(annotation);
        });

        // 修改交付状态
        ExaminationStateVO examinationListVo = new ExaminationStateVO();
        examinationListVo.setExaminationFlag(4);
        examinationListVo.setSlideId(slideId);
        // 修改tb_slide表中examination_flag状态（改为交付）
        ExaminationUtils.getSlide(examinationListVo, slideId);

        // 文件名
        String fileName = slideId + ExaminationResponseConstant.FILE_SUFFIX;

        // 生成Json字符串
        String jsonString = JSON.toJSONString(annotationList, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);

        // 以流的形式下载文件
        try {
            // 清空response
            response.reset();
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding(ExaminationConstant.CHARACTER_ENCODING);
            response.setContentType(ExaminationConstant.CONTENT_TYPE);
            response.setHeader(ExaminationConstant.HEADER, "attachment;filename=" + fileName);
            outputStream.write(jsonString.getBytes());
            // 关闭流
            outputStream.close();
        } catch (Exception e) {
            log.error(ExaminationResponseConstant.DOWNLOAD_ERROR, e);
        }

    }


    /**
     * 复合操作记录
     *
     * @param slideId
     * @return
     */
//    @RequiresPermissions(value = {"anno:examination:record"})
    @Log(title = "复核操作记录", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "获取操作记录接口")
    @GetMapping("/record")
    public R<PageMaster<ExaminationLogVO>> record(@RequestParam(value = "slideId")
                                                  @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) {
        startPage();
        List<ExaminationLogVO> examinationLogVOList = examinationLogService.selectBySlideId(slideId);
        for (ExaminationLogVO examinationLogVO : examinationLogVOList) {
            //设置审核状态名称
            examinationLogVO.setStatusName(ExaminationEnum.getEnumlabelByValue(examinationLogVO.getExaminationFlag()));
            //获取操作者信息
            SysUser userInformation = getUserInformationService.selectById(examinationLogVO.getCreateBy());
            //设置操作者名称
            examinationLogVO.setOperator(userInformation.getUserName());
        }
        PageMaster<ExaminationLogVO> pageMaster = new PageMaster<>(examinationLogVOList);
        return R.ok(pageMaster);
    }
}
