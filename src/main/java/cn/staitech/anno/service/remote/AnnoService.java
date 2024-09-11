package cn.staitech.anno.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.staitech.anno.vo.annotation.CategoryStatisticsIn;
import cn.staitech.common.core.domain.R;

/**
 * 
* @ClassName: SysUserService
* @Description:根据userId查询用户相关信息
* @author wanglibei
* @date 2024年9月4日
* @version V1.0
 */
@FeignClient(value = "staitech-anno", contextId = "annoService")
public interface AnnoService {
    /**
     * 通过userId获取用户信息
     */
    
	@PostMapping("/annotation/categoryStatistics")
    R<Boolean> categoryStatistics(@RequestBody CategoryStatisticsIn req);
    
    
}
