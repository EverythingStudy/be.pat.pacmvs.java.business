package cn.staitech.anno.service;

import cn.staitech.anno.domain.Image;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通过Feign调用注册在nacos中的python服务demo
 */
@FeignClient(value = "python-staitech-openslide", contextId = "pythonOpenslide")
public interface PythonOpenSlideService {

    /**
     * 获取用户信息
     * http://dev-python-staitech-openslide/index/index
     * */
    @GetMapping("/index/index")
    String test(@RequestParam("userId") Long userId);

    /**
     * 获取图片详情
     * http://dev-python-staitech-openslide/api/v1/image/19/getDetail
     * @param imageId
     * @return
     */
    @GetMapping("/api/v1/image/{image_id}/getDetailData")
    Image getImageDetail(@PathVariable("image_id") Long imageId);

}
