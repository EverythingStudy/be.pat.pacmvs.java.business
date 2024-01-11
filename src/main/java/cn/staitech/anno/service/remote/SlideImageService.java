package cn.staitech.anno.service.remote;

import cn.staitech.anno.vo.predictionInfo.in.PredictionInfoVO;
import cn.staitech.common.core.constant.SecurityConstants;
import cn.staitech.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlideImageService
 * @Description:切图处理
 * @date 2023年8月7日
 */
@FeignClient(contextId = "slideImageService", value = "staitech-openslide")
public interface SlideImageService {
    @PostMapping("/predictionImage/uploadImage")
    R uploadImage(@RequestBody PredictionInfoVO predictionInfoVO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
