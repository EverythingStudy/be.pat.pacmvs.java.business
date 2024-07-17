package cn.staitech.anno.controller;

import cn.staitech.anno.mapper.ContourRoiMapper;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.service.ContourRoiService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.uuid.UUID;
import cn.staitech.common.security.utils.SecurityUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Api(value = "viewer页面Roi", tags = "viewer页面Roi")
@RestController
@RequestMapping("/contourRoi")
public class ContourRoiController {

    @Resource
    private ContourRoiService contourRoiService;


    @Resource
    private ContourRoiMapper contourRoiMapper;

    @Resource
    private MarkingMapper markingMapper;


    @Resource
    private MarkingServiceV1 markingServiceV1;



    @ApiOperation(value = "获取GeoJson数据")
    @GetMapping("/test")
    public R<String> selectLists()  {
//        ContourRoi contourRoi = new ContourRoi();
//        contourRoi.setContourRoiId(1008L);
//        ContourRoi contourRoi2 = contourRoiMapper.selectBy(contourRoi);
//        contourRoi.setContourRoiId(1L);
//        ContourRoi contourRoi1 = contourRoiMapper.selectById(contourRoi);
        // 查询分表数据

//        Marking marking = markingServiceV1.getById("0007ebc8c2084dacbc36ff6a27c25348");

//        System.out.println(marking + "0--------------------------------->" );



//        System.out.println(contourRoi1);
//        System.out.println("----------------------------->");
//        System.out.println(contourRoi1 + ">>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(contourRoi2 + ">>>>>>>>>>>>>>>>>>>>>>>>>>");
        return R.ok("ok");
    }


    @ApiOperation(value = "获取GeoJson数据")
    @GetMapping("/selectList")
    public R<List<Features>> selectLists(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) throws Exception {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail(MessageSource.M("ARGUMENT_INVALID"));
        }
        return R.ok(contourRoiService.selectList(slideId));
    }


    @ApiOperation(value = "添加标注")
    @PostMapping("/insert")
    public R<Long> add(@Validated @RequestBody ViewAddIn req) throws Exception {
        req.setTraceId(UUID.fastUUID().toString());
        req.setIsBatch(false);
        if(req.getUpdate_by()==null){
            req.setUpdate_by(SecurityUtils.getLoginUser().getSysUser().getUserId());
        }
        Long contourRoiId = contourRoiService.insert(req);
        return R.ok(contourRoiId, MessageSource.M("OPERATE_SUCCEED"));
    }

    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @DeleteMapping("/delete")
    public R<String> del(@RequestParam(value = "marking_id") @ApiParam(name = "marking_id", value = "标注id", required = true) String marking_id) throws Exception {
        contourRoiService.delete(marking_id);
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }





}
