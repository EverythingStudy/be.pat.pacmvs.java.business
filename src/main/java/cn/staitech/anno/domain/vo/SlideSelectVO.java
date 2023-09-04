package cn.staitech.anno.domain.vo;

import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SlideSelectVO extends BaseEntity {
    private Long projectId;

    private Integer processFlag;

    private String description;

    private String imageName;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段:人工总数human_annotation_total、切片创建时间create_time、切片更新时间update_time", hidden = true)
    private String orderBy;

    /**
     * 排序顺序
     */
    @ApiModelProperty(value = "排序顺序:倒序desc、正序asc", hidden = true)
    private String sort;
}
