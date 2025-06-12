package cn.staitech.annotation.controller;

import cn.staitech.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mugw
 * @version 1.0
 * @description 测量标注表
 * @date 2025/5/21 14:33:43
 */
@RestController
@RequestMapping("/websocket")
public class WebsocketController {

    @Value("${netty.port}")
    private Integer port;

    @ApiOperation(value = "websocket接口")
    @GetMapping("/getWebsocketPort")
    public R<String> getWebsocketPort() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return R.fail();
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String localAdd = request.getLocalAddr();
        return R.ok("ws://" + localAdd + ":" + port + "/");
    }


}
