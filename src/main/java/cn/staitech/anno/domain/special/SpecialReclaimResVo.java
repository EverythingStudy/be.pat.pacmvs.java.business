package cn.staitech.anno.domain.special;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gjt.
 * @data 2023/5/31 10:01
 */
@Data
public class SpecialReclaimResVo {

    @ApiModelProperty(value = "专题id")
    private Long specialId;

    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(value = "专题名称")
    private String specialName;

    @ApiModelProperty(value = "种属")
    private String species;

    @ApiModelProperty(value = "种属")
    private String trialType;

    @ApiModelProperty(value = "染色类型")
    private String stainType;

    @ApiModelProperty(value = "项目数量")
    private Long projectNum;

    @ApiModelProperty(value = "切片数量")
    private Long slideNum;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "回收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String reclaimTime;

    @ApiModelProperty(value = "到期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String expireTime;

    @ApiModelProperty(value = "创建者名称")
    private String userName;


}
