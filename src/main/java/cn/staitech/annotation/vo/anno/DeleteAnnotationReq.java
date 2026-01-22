package cn.staitech.annotation.vo.anno;

import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import lombok.Data;

@Data
public class DeleteAnnotationReq  extends LogAuditBaseReq<AnnotationFeature> {

    @IgnoreLogField
    private Long annotationId;
}
