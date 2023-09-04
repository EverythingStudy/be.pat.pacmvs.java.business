package cn.staitech.anno.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description 脏器标签
 * @date 2023/6/26 15:44:34
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(" tb_viscera_tag")
public class VisceraTagPo {

    @ApiModelProperty(value = "脏器标签id")
    @TableId
    private Long tagId;

    @ApiModelProperty(value = "脏器id")
    private Long visceraId;

    @ApiModelProperty(value = "标签名称")
    private String name;

    @ApiModelProperty(value = "标签简称")
    private String alias;

    @ApiModelProperty(value = "色值")
    private String color;

    @ApiModelProperty(value = "切图算法匹配状态，0:未匹配，1：已匹配")
    private int matched;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
