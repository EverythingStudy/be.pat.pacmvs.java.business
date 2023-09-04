package cn.staitech.anno.domain.vo;

import lombok.Data;

@Data
public class SlideCategoryProcessFlagVO {
    
    private Long slideId;
    
    private Long processFlag;
    
    private Integer categoryId;

    private Long updateBy;
}
