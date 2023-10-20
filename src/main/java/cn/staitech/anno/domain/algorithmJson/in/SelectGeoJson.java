package cn.staitech.anno.domain.algorithmJson.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SelectGeoJson {

    @ApiModelProperty(value = "标签集合")
    private List<String> labelList;

    @ApiModelProperty(value = "json文件集合")
    private List<Long> algorithmJsonIdList;


}
