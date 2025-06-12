package cn.staitech.annotation.vo.anno;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author .
 */
@Data
public class AnnotationBatchReq{

    @NotNull(message = "{NO_SLIDE_DATA}")
    @JsonProperty("slide_id")
    private Long slideId;

    List<AnnotationBatchVo> list;
}
