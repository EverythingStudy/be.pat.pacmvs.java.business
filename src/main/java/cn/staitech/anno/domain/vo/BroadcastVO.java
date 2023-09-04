package cn.staitech.anno.domain.vo;

import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.marking.PointCount;
import lombok.Data;

import java.util.List;

@Data
public class BroadcastVO {

    private String type;

    private Long slideId;

    private Features data;

    private List<PointCount> point_count_list;
    
}
