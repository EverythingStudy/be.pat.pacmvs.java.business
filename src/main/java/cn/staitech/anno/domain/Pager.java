package cn.staitech.anno.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2023-06-02 15:04:13
 * @Description: 分页参数
 */

@Data
public class Pager {

    @ApiModelProperty(value = "页码")
    public int pageNum = 0;
    
    @ApiModelProperty(value = "每页显示多少条")
    public int pageSize = 10;
}
