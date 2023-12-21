package cn.staitech.anno.vo.species;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class InsertSpeciesVO {

    @NotNull(message = "{INSERTSPECIESVO.SPECIESID.ISNULL}")
    @Size(min = 0, max = 100, message = "{INSERTSPECIESVO.SPECIESID.LENGTH}")
    @ApiModelProperty(value = "种属ID", hidden = true)
    private String speciesId;

    @NotNull(message = "INSERTSPECIESVO.NAME.ISNULL")
    @Size(min = 0, max = 100, message = "{INSERTSPECIESVO.NAME.LENGTH}")
    @ApiModelProperty(value = "种属名称", required = true)
    private String name;

    @ApiModelProperty(value = "种属名称En", required = true)
    private String nameEn;

}