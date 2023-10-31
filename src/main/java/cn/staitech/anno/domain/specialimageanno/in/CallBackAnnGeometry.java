package cn.staitech.anno.domain.specialimageanno.in;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CallBackAnnGeometry {

    @ApiModelProperty(value = "标签名称")
    private String category_name;

    @ApiModelProperty(value = "标注坐标")
    private JSONObject geometry;

//	@ApiModelProperty(value = "标注数据类型(LineString,Polygon,point,pc,p,L)")
//	private String location_type;

}
