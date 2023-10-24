package cn.staitech.anno.domain.vo;

import lombok.Data;


@Data
public class ProjectAnnotationVO {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 数量
     */
    private Integer sum;
}
