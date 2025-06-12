package cn.staitech.annotation.utils.annotation;

import cn.staitech.annotation.domain.Annotation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/28 15:03:44
 */
@Builder
@Data
@Slf4j
public class UndoRedoDetail  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Annotation currentAnnotation;

    private Annotation historyAnnotation;

    @ApiModelProperty(value = "操作：修改-UPDATE,删除-DELETE,添加-INSERT")
    private String operation;

}
