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

    public int pageNum = 0;

    public int pageSize = 10;
}
