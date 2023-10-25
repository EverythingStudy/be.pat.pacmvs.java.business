package cn.staitech.anno.domain.user.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SelectUserIdListIn {

    @ApiModelProperty(value = "用户列表")
    private List<Long> userIdList;
}
