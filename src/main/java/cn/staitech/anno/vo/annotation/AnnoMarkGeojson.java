package cn.staitech.anno.vo.annotation;

import lombok.Data;

import java.util.List;

@Data
public class AnnoMarkGeojson {

    private String type = "FeatureCollection";

    private List<AnnoFeatures> features;


}
