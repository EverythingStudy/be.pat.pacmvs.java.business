package cn.staitech.anno.domain.algorithmJson.out;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SelectGeoJsonList {

    @ApiModelProperty(value = "用户列表")
    private List<Long> userList;


    @ApiModelProperty(value = "标签集合")
    private JSONArray labelInfoList;
}
