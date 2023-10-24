package cn.staitech.anno.service.remote;

import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.specialImageAnno.AlgorithmCutImageVO;
import cn.staitech.common.core.constant.SecurityConstants;
import cn.staitech.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlideImageService
 * @Description:切图处理
 * @date 2023年8月7日
 */
//@FeignClient(contextId = "SlideImageService", value = "staitech-openslide",configuration = FeignConfigure.class)
@FeignClient(contextId = "SlideImageService", value = "staitech-openslide")
public interface SlideImageService {

    @PostMapping("/slideNotice/slideImage")
    R cutImageNotice(@RequestBody AlgorithmCutImageVO cutVo, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/slideNotice/batchAddSpecialImage")
    R batchAddSpecialImage(List<SpecialImage> list, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
