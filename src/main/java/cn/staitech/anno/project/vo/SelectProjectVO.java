package cn.staitech.anno.project.vo;

import lombok.Data;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/13 17:44:08
 */
@Data
public class SelectProjectVO{
    
    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;
}
