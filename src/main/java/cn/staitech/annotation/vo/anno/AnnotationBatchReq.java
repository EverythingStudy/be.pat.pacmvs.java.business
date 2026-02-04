package cn.staitech.annotation.vo.anno;

import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author .
 */
@Data
public class AnnotationBatchReq{

    @IgnoreLogField
    @NotNull(message = "{NO_SLIDE_DATA}")
    @JsonProperty("slide_id")
    private Long slideId;

    @IgnoreLogField
    List<AnnotationBatchVo> list;

    /**
     * 主键id
     */
    @JsonProperty("marking_id")
    private String annotationId;
}
