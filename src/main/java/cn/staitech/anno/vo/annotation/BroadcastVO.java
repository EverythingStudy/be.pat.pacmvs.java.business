package cn.staitech.anno.vo.annotation;

import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.marking.PointCount;
import lombok.Data;

import java.util.List;

@Data
public class BroadcastVO {

    private String type;

    private Long slideId;

    private Features data;

    private List<Features> dataList;

    private List<PointCount> point_count_list;

}
