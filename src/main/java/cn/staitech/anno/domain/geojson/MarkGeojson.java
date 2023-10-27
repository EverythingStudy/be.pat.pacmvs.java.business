package cn.staitech.anno.domain.geojson;

import lombok.Data;

import java.util.List;

@Data
public class MarkGeojson {

    private String type = "FeatureCollection";

    private List<Features> features;

    private GeoImage image;

    private GeoProject project;


}
