package cn.staitech.anno.domain.vo.specialImageAnno;

import java.util.List;

import cn.staitech.anno.domain.marking.PointCount;
import lombok.Data;

@Data
public class AnnoBroadcastVO {

    private String type;

    private Long slideId;

    private AnnoFeatures data;

    private List<PointCount> point_count_list;
    
}
