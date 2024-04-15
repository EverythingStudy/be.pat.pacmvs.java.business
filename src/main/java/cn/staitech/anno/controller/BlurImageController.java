package cn.staitech.anno.controller;


import cn.staitech.anno.service.BlurImageService;
import cn.staitech.anno.vo.blurimage.in.ImageVagueQueryIn;
import cn.staitech.anno.vo.blurimage.out.ImageVagueListOutVO;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wmy
 * @since 2024-04-10
 */
@Api(value = "模糊切片", tags = "模糊切片")
@RestController
@RequestMapping("/blurImage")
public class BlurImageController {

    @Resource
    private BlurImageService blurImageService;

    @ApiOperationSupport(author = "wmy")
    @ApiOperation(value = "模糊切片列表")
    @PostMapping("/getImageVagueList")
    public R<PageResponse<ImageVagueListOutVO>> getImageVagueList(@RequestBody @Validated ImageVagueQueryIn req) throws ParseException {
        return R.ok(blurImageService.getImageVagueList(req));
    }

}

