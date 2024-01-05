package cn.staitech.anno.controller;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.Outline;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.service.OutlineService;
import cn.staitech.anno.utils.CustomizationIdUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.outline.OutlineSelectVO;
import cn.staitech.anno.vo.outline.OutlineStatistic;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.log.annotation.Log;
import cn.staitech.common.log.enums.BusinessType;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Viewer-吸管- (Outline)表控制层
 *
 * @author wangfeng
 * @since 2024-01-04 10:55:03
 */
@Slf4j
@Api(value = "Viewer-吸管", tags = "Viewer-吸管")
@RestController
@Validated
@RestControllerAdvice
@RequestMapping("/outline")
public class OutlineController {

    @Resource
    private OutlineService outlineService;

    @Resource
    private MarkingServiceV1 markingServiceV1;

    /**
     * Viewer-吸管-取消（删除所有当前用户的记录）
     * eg: /outline/clean?createBy=1
     *
     * @param createBy 创建者ID
     * @return true：成功，false：失败
     */
    @ApiOperation(value = "Viewer-吸管-取消")
    @Log(title = "Viewer-吸管-取消", menu = "Viewer-吸管-取消", subMenu = "Viewer-吸管-取消", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "createBy", value = "创建者ID", required = true, dataType = "Long", paramType = "query")})
    public R clean(@RequestParam @ApiParam(name = "createBy", value = "创建者ID", required = true) Long createBy) {
        outlineService.removeAllBycreateBy(createBy);
        return R.ok(MessageSource.M("OPERATE_SUCCEED"));
    }


    /**
     * Viewer-吸管-数据表
     *
     * @param selectVO
     * @return 所有数据
     */
    @ApiOperation(value = "Viewer-吸管-数据表")
    @Log(title = "Viewer-吸管-数据表", menu = "Viewer-吸管-数据表", subMenu = "Viewer-吸管-数据表", businessType = BusinessType.QUERY)
    @PostMapping("/list")
    public R<OutlineStatistic> list(@Validated @RequestBody OutlineSelectVO selectVO) {
        // 删除当前用户、非当前slideId的记录
        outlineService.removeBycreateBySlideId(selectVO.getCreateBy(), selectVO.getSlideId());

        if (selectVO.getMinVal() != null && selectVO.getMaxVal() != null && (selectVO.getMinVal() > selectVO.getMaxVal())) {
            return R.fail(MessageSource.M("OUTLINE.ARGUEMENT.ERROR"));
        }

        List<Outline> list = outlineService.selectList(selectVO);

        if (CollectionUtils.isEmpty(list)) {
            return R.fail(MessageSource.M("OUTLINE.NORESULT"));
        }
        // 查询业务类型：1面积(默认),2周长
        Integer bizType = selectVO.getBizType() != null ? selectVO.getBizType() : 1;
        return R.ok(outlineService.statistic(list, bizType));
    }

    /**
     * Viewer-吸管-保存为标注
     *
     * @param selectVO
     * @return
     */
    @ApiOperation(value = "Viewer-吸管-保存为标注")
    @Log(title = "Viewer-吸管-保存为标注", menu = "Viewer-吸管-保存为标注", subMenu = "Viewer-吸管-保存为标注", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R save(@Validated @RequestBody OutlineSelectVO selectVO) {
        if (selectVO.getMinVal() != null && selectVO.getMaxVal() != null && (selectVO.getMinVal() > selectVO.getMaxVal())) {
            return R.fail(MessageSource.M("OUTLINE.ARGUEMENT.ERROR"));
        }

        List<Outline> list = outlineService.selectList(selectVO);

        if (CollectionUtils.isEmpty(list)) {
            return R.fail(MessageSource.M("OUTLINE.NORESULT"));
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long organizationId = sysUser.getOrganizationId();
        String userName = sysUser.getUserName();

        List<cn.staitech.anno.project.domain.Marking> markingList = new ArrayList<>(list.size());

        for (Outline outline : list) {
            cn.staitech.anno.project.domain.Marking marking = new Marking();

            marking.setAnnotationId(CustomizationIdUtils.getSdId());
            marking.setCategoryId(selectVO.getCategoryId());
            marking.setGeometry(JSONObject.parseObject(outline.getGeometry()));
            marking.setImageId(outline.getImageId());
            marking.setSlideId(outline.getSlideId());
            marking.setPerimeter(outline.getPerimeter().toString());
            marking.setArea(outline.getArea().toString());
            marking.setOrganizationId(organizationId);
            marking.setProjectId(outline.getProjectId());
            marking.setCreateBy(outline.getCreateBy());
            marking.setCreateTime(new Date());
            marking.setAnnotationOwner(userName);
            marking.setAnnotationType(CommonConstant.ANNO_TYPE_DRAW);

            // 添加至列表中
            markingList.add(marking);
        }

        // 批量添加数据入库
        if (markingServiceV1.saveBatch(markingList)) {
            // 删除所有当前用户的记录
            outlineService.removeAllBycreateBy(selectVO.getCreateBy());
        }
        return R.ok(MessageSource.M("OPERATE_SUCCEED"));
    }
}
