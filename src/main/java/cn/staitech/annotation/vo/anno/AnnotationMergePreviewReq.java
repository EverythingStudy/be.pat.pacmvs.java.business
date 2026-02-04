package cn.staitech.annotation.vo.anno;

import cn.staitech.sft.logaudit.annotation.IgnoreLogField;
import cn.staitech.sft.logaudit.req.LogAuditBaseReq;
import lombok.Data;

import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/6/6 16:36:41
 */
@Data
public class AnnotationMergePreviewReq  {


    private List<Long> markingIdList;

    private Long slideId;
}
