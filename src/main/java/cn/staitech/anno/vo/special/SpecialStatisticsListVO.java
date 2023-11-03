package cn.staitech.anno.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description ：专题统计列表
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialStatisticsVO
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/15 星期四 22:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialStatisticsListVO implements Serializable {
    private static final long serialVersionUID = -5822907862588842506L;

    @ApiModelProperty(value = "专题ID")
    private Long specialId;

    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(value = "专题名称")
    private String specialName;

    @ApiModelProperty(value = "项目数")
    private Integer projectTotal;

    @ApiModelProperty(value = "已完成项目")
    private Integer projectComplete;

    @ApiModelProperty(value = "未完成项目")
    private Integer projectIncomplete;

    @ApiModelProperty(value = "专题完成率")
    private String specialCompletionRate;

    @ApiModelProperty(value = "状态(0待启动，1进行中，2暂停，3锁定，4已完成)")
    private String status;

    @ApiModelProperty(value = "删除标志(0:正常，1回收站，2删除)")
    private String delFlag;

    @ApiModelProperty(value = "用户ID")
    private String userId;
}
