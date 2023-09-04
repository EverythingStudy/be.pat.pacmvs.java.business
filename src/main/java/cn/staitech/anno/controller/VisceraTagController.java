package cn.staitech.anno.controller;

import cn.staitech.anno.domain.vo.VisceraTagVo;
import cn.staitech.anno.domain.vo.image.SubImageVo;
import cn.staitech.anno.service.VisceraTagService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 脏器标签
 * @date 2023/6/26 17:46:32
 */
@Api(value = "脏器标签", tags = "脏器标签")
@ApiSupport(author = "mgw")
@RestController
@RequestMapping("/visceraTag")
@Slf4j
public class VisceraTagController {

    @Autowired
    private VisceraTagService visceraTagService;

    @ApiOperation(value = "查询脏器标签操作")
    @PostMapping("/pageVisceraTag")
    public R<PageMaster<VisceraTagVo>> pageVisceraTag(@RequestBody Map params) {
        return visceraTagService.pageVisceraTag(params);
    }
}
