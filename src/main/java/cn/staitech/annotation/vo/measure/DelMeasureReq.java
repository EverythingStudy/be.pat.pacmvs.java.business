package cn.staitech.annotation.vo.measure;

import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import lombok.Data;

@Data
public class DelMeasureReq  extends LogAuditBaseReq {
    private Long marking_id;
}
