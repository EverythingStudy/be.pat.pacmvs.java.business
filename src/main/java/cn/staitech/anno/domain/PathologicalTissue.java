package cn.staitech.anno.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * aipre_pathological_tissue
 * @author 
 */
@Data
public class PathologicalTissue implements Serializable {
    /**
     * 病理组织id
     */
    private Long tissueId;

    /**
     * 病理组织名称
     */
    private String tissueName;

    /**
     * 病理组织英文名称
     */
    private String tissueNameEn;

    /**
     * 项目类型id
     */
    private Long projectTypeId;

    private static final long serialVersionUID = 1L;
}