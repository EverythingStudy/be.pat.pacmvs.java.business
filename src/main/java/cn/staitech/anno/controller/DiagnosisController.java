package cn.staitech.anno.controller;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.json.JSONUtil;
import cn.staitech.anno.constant.SpecialImageConstant;
import cn.staitech.anno.domain.diagnosis.SpecialDiagnosis;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisAddVo;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisDeleteVo;
import cn.staitech.anno.domain.vo.diagnosis.SpecialDiagnosisVo;
import cn.staitech.anno.domain.vo.diagnosis.SysDictResultVo;
import cn.staitech.anno.service.GetUserInformationService;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.SpecialDiagnosisService;
import cn.staitech.anno.service.SpecialImageAnnoService;
import cn.staitech.anno.service.SpecialImageService;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.service.SysDictDataService;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: DiagnosisController
 * @Description:人工诊断
 * @author wanglibei
 * @date 2023年6月28日
 * @version V1.0
 */
@Slf4j
@Api(value = "人工诊断接口", tags = "人工诊断接口")
@RestController
@Validated
@RequestMapping("/diagnosis")
public class DiagnosisController {

	DecimalFormat decimalFormat = new DecimalFormat("0.00000");

	@Resource
	private SpecialImageService specialImageService;

	@Resource
	private GetUserInformationService getUserInformationService;

	@Resource
	private SpecialImageAnnoService specialImageAnnoService;

	@Resource
	private SubImageService subImageService;

	@Resource
	private RabbitTemplate rabbitTemplate;

	@Resource
	private ImageService imageService;

	@Resource
	private SysDictDataService sysDictDataService;

	@Resource
	private ProjectService projectService;
	@Resource
	private SpecialDiagnosisService specialDiagnosisService;
	
	/**
	 * 
	* @Title: add
	* @Description: 人工诊断添加
	* @param @param addVo
	* @param @return
	* @param @throws Exception
	* @return R
	* @throws
	 */
	@ApiOperation(value = "添加诊断接口")
	@Log(title = "人工诊断添加", menu = "人工诊断添加", subMenu = "保存诊断结果", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	public R add(@Validated @RequestBody List<SpecialDiagnosisAddVo> addVoList){
		long startTime = System.currentTimeMillis();
		if (CollectionUtils.isNotEmpty(addVoList)) {
			int  checkFlage = specialDiagnosisService.saveOrUpdateSpecialDiagnosisVo(addVoList);
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			log.info("人工诊断程序总运行时间： " + totalTime + " 毫秒");
			if(checkFlage == -1){
				return R.fail(SpecialImageConstant.Diagnosis_PARAMS_EXCEPTION);
			}else if(checkFlage == -2){
				return R.fail(SpecialImageConstant.Diagnosis_PARAMS_CHECK);
			}else{
				return R.ok();
			}
			
		}else {
			return R.fail(SpecialImageConstant.Data_NULL);
		}
	}

	
	
	@ApiOperation(value = "删除诊断接口")
	@Log(title = "人工诊断删除", menu = "人工诊断删除", subMenu = "修改诊断结果", businessType = BusinessType.UPDATE)
	@PostMapping("/delete")
	public R delete(@Validated @RequestBody SpecialDiagnosisDeleteVo specialDiagnosisDeleteVo) throws Exception {
		if (null != specialDiagnosisDeleteVo) {
			SpecialDiagnosis sid = specialDiagnosisService.getSpecialDiagnosis(specialDiagnosisDeleteVo.getSpecialDiagnosisId());
			if(null == sid){
				return R.fail(SpecialImageConstant.Data_NULL);
			}
			//判断当前人和编辑人是否是同一个人
			Long currentUserId = SecurityUtils.getUserId();
			if(!currentUserId.equals(sid.getCreateBy())){
				return R.fail(SpecialImageConstant.Diagnosis_DELETE_CHECK);
			}
			specialDiagnosisService.deleteSpecialDiagnosisVo(specialDiagnosisDeleteVo.getSpecialDiagnosisId());
			//刷新列表数据返回
//			List<SpecialDiagnosisVo> list = specialDiagnosisService.getSpecialDiagnosisVo(sid.getSubImageId()+"",sid.getProjectId()+"",sid.getSpecialId()+"",sid.getGroupId()+"");
//			return R.ok(list);
			return R.ok();
		} else {
			return R.fail(SpecialImageConstant.Data_NULL);
		}
	}
	

	/**
	 * 
	* @Title: info
	* @Description: 查询人工诊断列表
	* @param @param specialImageId
	* @param @param projctId
	* @param @param specialId
	* @param @return
	* @return R
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询人工诊断列表")
//	@Log(title = "查询人工诊断列表", menu = "专题阅片", subMenu = "项目列表", businessType = BusinessType.QUERY)
	@GetMapping("/info")
	public R info( 
			@RequestParam @ApiParam(name = "subImageId", value = "切片id", required = true) String subImageId,
			@RequestParam @ApiParam(name = "projectId", value = "项目id", required = true) String projectId,
			@RequestParam @ApiParam(name = "groupId", value = "分组id", required = true) String groupId,
			@RequestParam @ApiParam(name = "specialId", value = "专题id", required = true) String specialId) {
		//通过项目ID 专题id 切片id 查询所有的诊断结果，返回列表（添加是否可以修改）
		List<SpecialDiagnosisVo> list = specialDiagnosisService.getSpecialDiagnosisVo(subImageId,projectId,specialId,groupId);
		return R.ok(list);
	}

	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "搜索标签列表")
	@Log(title = "搜索标签列表",menu = "专题阅片",subMenu = "项目列表-标签搜索",businessType = BusinessType.QUERY)
	@GetMapping("/getAllTag")
	public R getAllTag() {
		SysDictResultVo vo = specialDiagnosisService.getSysDictResultVo();
		return R.ok(vo);
	}
}