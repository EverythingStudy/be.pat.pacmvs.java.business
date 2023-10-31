package cn.staitech.anno.domain.specialimageanno;

import lombok.Data;

import java.util.List;

@Data
public class AnnoMarkGeojson {

    private String type = "FeatureCollection";

    private List<AnnoFeatures> features;


}
