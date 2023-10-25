package cn.staitech.anno.domain.vo.specialimageanno;

import lombok.Data;

import java.util.List;

@Data
public class AnnoMarkGeojson {

    private String type = "FeatureCollection";

    private List<AnnoFeatures> features;


}
