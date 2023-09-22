package cn.staitech.anno.project.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description 评审轮次查询条件
 * @date 2023/9/22 13:41:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRoundIN {

    /**
     * 评审内容
     */
    @ApiModelProperty("项目id")
    private Long projectId;

    /**
     * 评审内容
     */
    @ApiModelProperty("评审内容")
    private String reviewContent;
    /**
     * 专题编号
     */
    @ApiModelProperty("专题编号")
    private String topicName;
    /**
     * 评审轮次ID、对应1至10轮
     */
    @ApiModelProperty("评审轮次ID、对应1至10轮")
    private Long roundId;

    /**
     * 组别ID、对应group1至8
     */
    @ApiModelProperty("组别ID、对应group1至8")
    private Long groupId;


}
