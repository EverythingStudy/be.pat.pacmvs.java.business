package cn.staitech.anno.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SelectUserIdListIn {

    @ApiModelProperty(value = "用户列表")
    private List<Long> userIdList;
}
