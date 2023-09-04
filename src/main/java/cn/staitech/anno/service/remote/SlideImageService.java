package cn.staitech.anno.service.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import cn.staitech.anno.config.FeignConfigure;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import cn.staitech.anno.domain.vo.specialImageAnno.AlgorithmCutImageVO;
import cn.staitech.common.core.constant.SecurityConstants;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: SlideImageService
* @Description:切图处理
* @author wanglibei
* @date 2023年8月7日
* @version V1.0
 */
//@FeignClient(contextId = "SlideImageService", value = "staitech-openslide",configuration = FeignConfigure.class)
@FeignClient(contextId = "SlideImageService", value = "staitech-openslide")
public interface SlideImageService {
	
	@PostMapping("/slideNotice/slideImage")
    R cutImageNotice(@RequestBody AlgorithmCutImageVO cutVo ,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
	
	@PostMapping("/slideNotice/batchAddSpecialImage")
    R batchAddSpecialImage(List<SpecialImage> list, @RequestHeader(SecurityConstants.FROM_SOURCE) String source); 

}
