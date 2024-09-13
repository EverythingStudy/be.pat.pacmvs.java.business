package cn.staitech.anno.vo.accessprojectrecords;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccessProjectRecordsOut {

    @ApiModelProperty(value = "访问数量")
   private Integer num;

    @ApiModelProperty(value = "访问时间")
    private String accessTime;
}
