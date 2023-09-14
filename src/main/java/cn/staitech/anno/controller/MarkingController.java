package cn.staitech.anno.controller;


import cn.staitech.anno.constant.R.ResponseConstant;
import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.document.GeometryDoc;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.geojson.in.MarkingUpdateIn;
import cn.staitech.anno.domain.geojson.in.viewAddIn;
import cn.staitech.anno.domain.marking.Marking;
import cn.staitech.anno.domain.marking.PointCount;
import cn.staitech.anno.domain.vo.BroadcastVO;
import cn.staitech.anno.domain.vo.NettyPortVO;
import cn.staitech.anno.domain.vo.marking.out.MarkingSelectListVo;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.SendMessage;
import cn.staitech.common.core.domain.R;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




/**
 * @author gjt
 * @since 2023-09-14
 */
@RestController
@RequestMapping("/marking")
public class MarkingController {

    @Resource
    private MarkingService markingService;

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "获取标注列表")
    @GetMapping("/selectList")
    public R<List<MarkingSelectListVo>> selectList(@RequestParam(value = "slideId") @ApiParam(name = "slideId", value = "切片ID", required = true) Long slideId) {
        if (!Optional.ofNullable(slideId).isPresent()) {
            return R.fail("参数异常");
        }
        return R.ok(markingService.selectList(slideId));
    }
    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "添加标注")
    @PostMapping("/insert")
    public R<Long> add(@Validated @RequestBody viewAddIn req) throws Exception {
        Long markingId = markingService.insert(req);
        return R.ok(markingId, ResponseConstant.OPERATE_SUCCEED);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "删除标注")
    @ApiImplicitParams({@ApiImplicitParam(name = "markingId", value = "标注id", required = true, dataType = "Long", paramType = "query")})
    @DeleteMapping("/delete")
    public R<String> del(@RequestParam(value = "marking_id") @ApiParam(name = "marking_id", value = "标注id", required = true) Long marking_id) throws Exception {
        markingService.delete(marking_id);
        return R.ok("操作成功");
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "更新标注")
    @PutMapping("/update")
    public R<Long> update(@Validated @RequestBody MarkingUpdateIn req) throws Exception {
        markingService.update(req);
        return R.ok(req.getMarking_id(), ResponseConstant.OPERATE_SUCCEED);
    }

    @ApiOperationSupport(author = "gjt")
    @ApiOperation(value = "websocket接口", hidden = true)
    @GetMapping("/getWebsocketPort")
    public R<String> getWebsocketPort() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return R.fail(ResponseConstant.OPERATE_ERROR);
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String localAdd = request.getLocalAddr();
        return R.ok("ws://" + localAdd + ":" + 9999 + "/");
    }

}

