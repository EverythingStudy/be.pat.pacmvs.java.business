package cn.staitech.anno.domain.vo.specialimageanno;

import cn.staitech.anno.domain.marking.PointCount;
import lombok.Data;

import java.util.List;

@Data
public class AnnoBroadcastVO {

    private String type;

    private Long slideId;

    private AnnoFeatures data;

    private List<PointCount> point_count_list;

}
