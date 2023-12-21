package cn.staitech.anno.vo.organ;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class InsertOrganVO {

    @NotNull(message = "{InsertOrganVO.ORGANID.ISNULL}")
    @Size(min = 0, max = 100, message = "{InsertOrganVO.ORGANID.LENGTH}")
    @ApiModelProperty(value = "脏器ID", hidden = true)
    private String organId;

    @NotNull(message = "InsertOrganVO.NAME.ISNULL")
    @Size(min = 0, max = 100, message = "{InsertOrganVO.NAME.LENGTH}")
    @ApiModelProperty(value = "脏器名称", required = true)
    private String name;

    @ApiModelProperty(value = "脏器名称En", required = true)
    private String nameEn;

    @ApiModelProperty(value = "种属编码", required = true)
    @NotNull(message = "{InsertOrganVO.SPECIESCODE.ISNULL}")
    private String speciesCode;

}