package cn.staitech.annotation.vo.anno;

import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteAnnotationReq  extends LogAuditBaseReq<AnnotationFeature> {
    /**
     * 主键id
     */
    @JsonProperty("marking_id")
    @IgnoreLogField
    private Long annotationId;
}
