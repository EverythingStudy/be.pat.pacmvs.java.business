package cn.staitech.anno.vo.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2024-02-21 18:10:43
 * @Description:
 */
@Data
public class Cursor {
    @ApiModelProperty(value = "undo")
    private Boolean undo = false;

    @ApiModelProperty(value = "redo")
    private Boolean redo = false;

    private Integer index;
    private Integer size;
}
