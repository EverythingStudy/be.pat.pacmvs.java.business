package cn.staitech.anno.project.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/13 17:44:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPartUserVO{
    
	 /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户姓名
     */
    private String nickName;
    
}
