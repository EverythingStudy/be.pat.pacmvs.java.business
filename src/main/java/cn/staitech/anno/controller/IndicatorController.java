package cn.staitech.anno.controller;

import static cn.staitech.common.security.utils.SecurityUtils.isAdmin;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.Indicator;
import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.mapper.OrganMapper;
import cn.staitech.anno.service.IndicatorService;
import cn.staitech.anno.service.PathologicalIndicatorCategoryService;
import cn.staitech.anno.service.ProjectService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.indicator.IndicatorAddVO;
import cn.staitech.anno.vo.indicator.IndicatorGetVO;
import cn.staitech.anno.vo.indicator.IndicatorListVO;
import cn.staitech.anno.vo.indicator.IndicatorReviseVO;
import cn.staitech.anno.vo.indicator.IndicatorVO;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.web.controller.BaseController;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.annotation.Logical;
import cn.staitech.common.security.annotation.RequiresPermissions;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;


/**
 * @author wangfeng
 * @Date 2023/09/14 15:50
 * @desc 结构指标
 */
@Api(value = "结构指标接口", tags = "结构指标")
@RestController
@RequestMapping("/indicator")
public class IndicatorController extends BaseController {
    @Resource
    private IndicatorService indicatorService;
    @Resource
    private ProjectService projectService;
    @Resource
    private PathologicalIndicatorCategoryService pathologicalService;

    @Resource
    private OrganMapper organMapper;

    @Resource
    private StructureService structureService;

    /**
     * 添加结构指标 2.0SAAS .
     */
    @SneakyThrows
    @RequiresPermissions(value = {"project:pathology:define", "project:pathology:add"}, logical = Logical.OR)
    @ApiOperation(value = "添加结构指标", notes = "wangfeng")
    @Log(title = "添加结构指标", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public R<String> add(@Validated @RequestBody IndicatorAddVO req) {
        //标签类型 0:下拉筛选标签；1:自定义标签
        Integer indicatorType = req.getIndicatorType();
        if (null == indicatorType) {
            indicatorType = 0;
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long organizationId = sysUser.getOrganizationId();
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(req.getSpeciesId());
        indicator.setOrganId(req.getOrganId());
        indicator.setOrganizationId(organizationId);
        indicator.setDelFlag(0);
        // 查询结构指标是否存在
        List<Indicator> indicatorList = indicatorService.selectIndicator(indicator);
        if (!indicatorList.isEmpty()) {
            return R.fail(MessageSource.M("INDICATOR_EXIST"));
        }
        if (indicatorType == 1) {
            //校验脏器名称是否已经重复
            QueryWrapper<Organ> queryNameWrapper = new QueryWrapper<>();
            queryNameWrapper.eq("name", req.getOrganName());
            queryNameWrapper.eq("organization_id", organizationId);
            List<Organ> nameList = organMapper.selectList(queryNameWrapper);
            if (CollectionUtils.isNotEmpty(nameList)) {
                return R.fail(MessageSource.M("InsertOrganVO.NAME.EXIST"));
            }


            //校验脏器编码 是否已经重复
            QueryWrapper<Organ> queryOrganIdWrapper = new QueryWrapper<>();
            queryOrganIdWrapper.eq("organ_id", req.getOrganId());
            queryOrganIdWrapper.eq("organization_id", organizationId);

            List<Organ> organWrapperList = organMapper.selectList(queryOrganIdWrapper);
            if (CollectionUtils.isNotEmpty(organWrapperList)) {
                return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
            }

            Organ organ = new Organ();
            organ.setName(req.getOrganName());
            organ.setNameEn(req.getOrganName());
            organ.setOrganId(req.getOrganId());
            organ.setSpeciesCode(req.getSpeciesId());
            organ.setOrganizationId(organizationId);

            //先查询是否有这个脏器
            QueryWrapper<Organ> queryOrganEditWrapper = new QueryWrapper<>();
            queryOrganEditWrapper.eq("organ_id", req.getOrganId());
            queryOrganEditWrapper.eq("name", req.getOrganName());
            queryOrganEditWrapper.eq("species_code", req.getSpeciesId());
            queryOrganEditWrapper.eq("organization_id", organizationId);
            List<Organ> organEditWrapperList = organMapper.selectList(queryOrganEditWrapper);
            if (CollectionUtils.isNotEmpty(organEditWrapperList)) {

            } else {
                organ.setNameEn(req.getOrganName());
                organMapper.insert(organ);
            }


            MapConstant.ORGAN_MAP = structureService.selectMap();
            MapConstant.ORGAN_MAP_EN = structureService.selectMapEn();
            MapConstant.STRUCTURE_MAP = structureService.selectMap();
            MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
        }

        if (indicatorType == 0) {
            // 20231222wangfeng
            indicator.setIndicatorName(MapConstant.getOrgan(organizationId + req.getSpeciesId() + req.getOrganId()));
            indicator.setIndicatorNameEn(MapConstant.getOrganEn(organizationId + req.getSpeciesId() + req.getOrganId()));
        } else {
            indicator.setIndicatorName(req.getOrganName());
            indicator.setIndicatorNameEn(req.getOrganName());
        }
        indicator.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
        indicator.setCreateBy(sysUser.getUserId());
//				indicator.setCreateBy(1L);
        //20231107wd结构指标关联机构
        indicator.setOrganizationId(organizationId);
        indicator.setIndicatorType(indicatorType);
        //添加结构指标
        indicatorService.insertIndicator(indicator);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    /**
     * 获取结构指标列表 .
     */
    @ApiOperation(value = "查询结构指标接口", notes = "wangfeng")
    @RequiresPermissions("project:pathology:query")
    @Log(title = "结构指标列表", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.QUERY)
    @PostMapping("/allIndicator")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前记录起始索引", dataTypeClass = Integer.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", dataTypeClass = Integer.class, paramType = "query", example = "10")})
    public R<PageMaster<Indicator>> list1(@RequestBody IndicatorListVO indicatorListVO) {
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorListVO, indicator);
        //20231107wd_机构
        indicator.setOrganizationId(indicatorListVO.getOrganizationId());

        if (indicatorListVO.getOrganizationId() == null || indicatorListVO.getOrganizationId() < 1) {
            if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
                indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
            }
        }

        PageMaster<Indicator> pageMaster = indicatorService.selectIndicatorList(indicator, indicatorListVO.getPageNum(), indicatorListVO.getPageSize());
        return R.ok(pageMaster);
    }

    /**
     * 根据指标id获取详细信息 .
     */
    @ApiOperation(value = "获取病例指标详情接口", notes = "ZMJ")
    @GetMapping(value = "/details")
    public R<IndicatorVO> getInfo(
            @RequestParam @ApiParam(name = "indicatorId", value = "病理指标id", required = true) Long indicatorId) {
        // 获取病理信息
        Indicator indicator = indicatorService.selectIndicatorsById(indicatorId);
        IndicatorVO indicatorVo = new IndicatorVO();
        if (indicator != null) {
            // 浅拷贝
            BeanUtils.copyProperties(indicator, indicatorVo);
        }
        //添加关联项目
        indicatorVo.setProjectVo(projectService.selectProjectInfo(indicatorId));
        return R.ok(indicatorVo);
    }


    /**
     * 病理指标删除接口 .
     */
    @ApiOperation(value = "病理指标删除接口", notes = "ZMJ")
    @RequiresPermissions("project:pathology:remove")
    @Log(title = "病理指标删除接口", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
    @PostMapping("/del")
    public R<String> delIndicator(@RequestBody IndicatorGetVO indicatorGetVO) {
        if (!Optional.ofNullable(indicatorGetVO.getIndicatorId()).isPresent()) {
            return R.fail(MessageSource.M("INDICATOR_ID_NOTNULL"));
        }
        
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        Indicator indicator = new Indicator();
        indicator.setOrganizationId(organizationId);
        indicator.setIndicatorId(indicatorGetVO.getIndicatorId().longValue());
        Integer num = indicatorService.selectIndicatorCountByIndicator(indicator);
//        Integer num = indicatorService.selectIndicatorCountInProject(indicatorGetVO.getIndicatorId().longValue());
        if (num > 0) {
            return R.fail(MessageSource.M("ALREADY_BOUND_NO_DEL"));
        }
        //删除标注类别
        PathologicalIndicatorCategory Pathological = PathologicalIndicatorCategory.builder().indicatorId(indicatorGetVO.getIndicatorId().longValue()).organizationId(organizationId).delFlag(1).build();
        pathologicalService.updateByPrimaryKeySelective(Pathological);
        //删除病理指标
        indicatorService.delIndicator(indicatorGetVO.getIndicatorId().longValue());
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }


    /**
     * 病理指标修改接口 .
     */
    @ApiOperation(value = "病理指标修改接口", notes = "ZMJ")
    @RequiresPermissions("project:pathology:edit")
    @Log(title = "病理指标修改接口", menu = "专题管理", subMenu = "病理指标", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public R<Integer> edit(@Validated @RequestBody IndicatorReviseVO req) {
        // 和项目绑定的不能修改
//        Integer num = indicatorService.selectIndicatorCountInProject(req.getIndicatorId().longValue());
    	Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        Indicator indicatorQuery = new Indicator();
        indicatorQuery.setOrganizationId(organizationId);
        indicatorQuery.setIndicatorId(req.getIndicatorId().longValue());
        Integer num = indicatorService.selectIndicatorCountByIndicator(indicatorQuery);
        if (num > 0) {
            return R.fail(MessageSource.M("ALREADY_BOUND"));
        }

        //标签类型 0:下拉筛选标签；1:自定义标签
        Integer indicatorType = req.getIndicatorType();
        if (null == indicatorType) {
            indicatorType = 0;
        }


        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        //		Long organizationId = 1L;
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(req.getSpeciesId());
        indicator.setOrganId(req.getOrganId());
        indicator.setOrganizationId(organizationId);
        indicator.setDelFlag(0);
        // 查询结构指标是否存在
        List<Indicator> indicatorList = indicatorService.selectIndicator(indicator);
        if (!indicatorList.isEmpty()) {
            //			return R.fail(MessageSource.M("INDICATOR_EXIST"));
            boolean idCheck = true;
            for (Indicator indicatorP : indicatorList) {
                String organ_id = indicatorP.getOrganId();
                if (!organ_id.equals(req.getOrganId())) {
                    idCheck = false;
                    break;
                }
            }
            if (!idCheck) {
                return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
            }

        }


        if (indicatorType == 1) {
            Organ organ = new Organ();
            organ.setName(req.getOrganName());
            organ.setOrganId(req.getOrganId());
            organ.setSpeciesCode(req.getSpeciesId());
            organ.setOrganizationId(organizationId);
            //先查询是否有这个脏器
            QueryWrapper<Organ> queryOrganEditWrapper = new QueryWrapper<>();
            queryOrganEditWrapper.eq("organ_id", req.getOrganId());
            queryOrganEditWrapper.eq("species_code", req.getSpeciesId());
            queryOrganEditWrapper.eq("organization_id", organizationId);
            List<Organ> organEditWrapperList = organMapper.selectList(queryOrganEditWrapper);
            if (CollectionUtils.isNotEmpty(organEditWrapperList)) {
                Organ o1 = organEditWrapperList.get(0);
                UpdateWrapper<Organ> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("organ_id", o1.getOrganId());
                updateWrapper.eq("species_code", o1.getSpeciesCode());
                updateWrapper.eq("organization_id", organizationId);
                updateWrapper.set("name", req.getOrganName());
                updateWrapper.set("name_en", req.getOrganName());

                organMapper.update(null, updateWrapper);
            } else {
                organ.setNameEn(req.getOrganName());
                organMapper.insert(organ);
            }


            MapConstant.ORGAN_MAP = structureService.selectMap();
            MapConstant.ORGAN_MAP_EN = structureService.selectMapEn();
            MapConstant.STRUCTURE_MAP = structureService.selectMap();
            MapConstant.STRUCTURE_MAP_EN = structureService.selectMapEn();
        }

        if (indicatorType == 0) {
            indicator.setIndicatorName(MapConstant.getOrgan(organizationId + req.getSpeciesId() + req.getOrganId()));
            indicator.setIndicatorNameEn(MapConstant.getOrganEn(organizationId + req.getSpeciesId() + req.getOrganId()));
        } else {
            indicator.setIndicatorName(req.getOrganName());
            indicator.setIndicatorNameEn(req.getOrganName());
            req.setIndicatorName(req.getOrganName());
        }
        indicator.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
        indicator.setCreateBy(sysUser.getUserId());

        req.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
        // 修改病理指标
        indicatorService.updateIndicator(req);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));

    }

    @ApiOperation(value = "病理指标查重接口", notes = "ZMJ")
    @GetMapping("/check")
    public R<Integer> checkEdit(@RequestParam @ApiParam(name = "indicatorId", value = "病理指标id", required = true) Long indicatorId) {
        Long organizationId = SecurityUtils.getLoginUser().getSysUser().getOrganizationId();
        Indicator indicator = new Indicator();
        indicator.setOrganizationId(organizationId);
        indicator.setIndicatorId(indicatorId);
//        Integer num = indicatorService.selectIndicatorCountInProject(indicatorId);
        Integer num = indicatorService.selectIndicatorCountByIndicator(indicator);
        if (0 < num) {
            return R.fail(MessageSource.M("ALREADY_BOUND"));
        }
        return R.ok(1);
    }

    /**
     * 关联病理指标列表 2.0SAAS .
     */
    @ApiOperation(value = "关联病理指标列表", notes = "wangfeng")
    @GetMapping("/getIndicatorList")
    public R<List<Indicator>> getIndicatorList(@Validated @RequestParam String speciesId) {
        clearPage();
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(speciesId);
        //20231107wd补充需求机构
        if (!isAdmin(SecurityUtils.getUserId())) {
            indicator.setOrganizationId(SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }
        List<Indicator> list = indicatorService.selectIndicatorInformation(indicator);
        return R.ok(list);
    }


    //	@SneakyThrows
    //	@ApiOperation(value = "添加结构指标-New")
    //	@Log(title = "添加结构指标", menu = "结构指标", subMenu = "结构指标", businessType = BusinessType.INSERT)
    //	@PostMapping("/save")
    public R<String> save(@Validated @RequestBody IndicatorAddVO req) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long organizationId = sysUser.getOrganizationId();
        int saveCheck = indicatorService.saveCheck(req);
        if (saveCheck != 0) {
            if (saveCheck == 1) {
                return R.fail(MessageSource.M("INSERTSPECIESVO.NAME.EXIST"));
            } else if (saveCheck == 2) {
                return R.fail(MessageSource.M("INSERTSPECIESVO.SPECIESID.EXIST"));
            } else if (saveCheck == 3) {
                return R.fail(MessageSource.M("InsertOrganVO.NAME.EXIST"));
            } else if (saveCheck == 4) {
                return R.fail(MessageSource.M("InsertOrganVO.ORGANID.EXIST"));
            }
        }
        Indicator indicator = new Indicator();
        indicator.setSpeciesId(req.getSpeciesId());
        indicator.setOrganId(req.getOrganId());
        indicator.setOrganizationId(organizationId);
        indicator.setDelFlag(0);
        // 查询结构指标是否存在
        List<Indicator> indicatorList = indicatorService.selectIndicator(indicator);
        if (!indicatorList.isEmpty()) {
            return R.fail(MessageSource.M("INDICATOR_EXIST"));
        }
        // 20231222wangfeng
        String indicatorName = MapConstant.getOrgan(organizationId + req.getSpeciesId() + req.getOrganId());
        if (StringUtils.isEmpty(indicatorName)) {
            indicatorName = req.getOrganName();
        }
        indicator.setIndicatorName(indicatorName);
        // 20231222wangfeng
        String indicatorNameEn = MapConstant.getOrganEn(organizationId + req.getSpeciesId() + req.getOrganId());
        if (StringUtils.isEmpty(indicatorNameEn)) {
            indicatorNameEn = req.getOrganName();
        }
        indicator.setIndicatorNameEn(indicatorNameEn);
        indicator.setNumber(indicator.getSpeciesId().concat(indicator.getOrganId()));
        indicator.setCreateBy(sysUser.getUserId());
        //20231107wd结构指标关联机构
        indicator.setOrganizationId(organizationId);
        //添加结构指标
        indicatorService.insertIndicator(indicator);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

}
