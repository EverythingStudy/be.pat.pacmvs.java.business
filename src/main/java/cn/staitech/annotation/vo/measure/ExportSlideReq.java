package cn.staitech.annotation.vo.measure;

import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import lombok.Data;

@Data
public class ExportSlideReq extends LogAuditBaseReq {
    private Long slideId;
}
