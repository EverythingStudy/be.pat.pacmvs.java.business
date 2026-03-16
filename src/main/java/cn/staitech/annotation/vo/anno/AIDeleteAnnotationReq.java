package cn.staitech.annotation.vo.anno;

import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AIDeleteAnnotationReq {
    /**
     * 主键id
     */
    @JsonProperty("marking_id")
    @IgnoreLogField
    private Long annotationId;
}
