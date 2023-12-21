package cn.staitech.anno.controller;

import cn.staitech.anno.service.PathologicalTissueService;
import cn.staitech.anno.vo.pathologicaltissue.PathologicalTissueVO;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "病理组织", tags = "病理组织")
@RestController
@RequestMapping("/tissue")
@Slf4j
public class PathologicalTissueController {

    @Resource
    private PathologicalTissueService pathologicalTissueService;

    @ApiOperationSupport(author = "zmj")
    @ApiOperation(value = "拼接图象类项目---病理组织列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectTypeId", value = "项目类型id", dataTypeClass = Long.class, paramType = "query")})
    @GetMapping("/list")
    public R<List<PathologicalTissueVO>> getTissueList(@RequestParam("projectTypeId") Long projectTypeId) {
        List<PathologicalTissueVO> pathologicalTissueVOList = pathologicalTissueService.selectByPrimaryKey(projectTypeId);
        return R.ok(pathologicalTissueVOList);
    }
}
