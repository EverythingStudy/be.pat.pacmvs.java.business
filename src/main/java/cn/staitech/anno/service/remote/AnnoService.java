package cn.staitech.anno.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.staitech.anno.vo.annotation.CategoryStatisticsIn;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: AnnoService
* @Description:根据标签查询是否被使用
* @author wanglibei
* @date 2024年9月12日
* @version V1.0
 */
@FeignClient(value = "staitech-fr", contextId = "annoService")
public interface AnnoService {
    
	@PostMapping("/annotation/categoryStatistics")
    R<Boolean> categoryStatistics(@RequestBody CategoryStatisticsIn req);
    
    
}
