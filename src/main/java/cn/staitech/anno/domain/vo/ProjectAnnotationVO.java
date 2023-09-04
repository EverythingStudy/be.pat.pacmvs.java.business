package cn.staitech.anno.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;


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
