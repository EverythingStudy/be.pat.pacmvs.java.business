package cn.staitech.anno.vo.marking;

import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.GeoImage;
import cn.staitech.anno.vo.geojson.GeoProject;
import lombok.Data;

import java.util.List;

@Data
public class MarkGeojson {

    private String type = "FeatureCollection";

    private List<Features> features;

    private GeoImage image;

    private GeoProject project;


}
