package cn.staitech.anno.service.impl.manage;

import cn.staitech.anno.domain.Annotation;
import cn.staitech.anno.domain.Slide;
import com.vividsolutions.jts.geom.Geometry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ROI 无属性标注
 */
@Slf4j
@Service
public class AnnotationROIManage {

    /**
     * 返回Geometry最小边界矩形ROI标注列表：无属性标注 1转N 512*512 1024*1024 end
     * <p>
     * MBR:The Minimum Bounding Rectangle 最小边界矩形
     * 参考： https://blog.csdn.net/qq_40985985/article/details/127034254
     * geom.getEnvelope(); // 外接矩形4个点 得到外接矩形，不一定是面积最小；可以对多边形的每一条边求外接矩形，然后比较得到最小外接矩形
     * geom.getEnvelopeInternal(); // 外接矩形对角线两点
     * geom.getBoundary(); // 首尾俩点
     * (new ConvexHull(geom)).getConvexHull(); // 获取凸包（凸包可以理解为线，多线闭合后的多边形）
     * <p>
     * 3步进行求解；
     * 1. 获取Envelope的外接矩形，获取面积；
     * 2. 获取凸包，旋转获取每一条边对应的外接矩形，得到凸包面积最小外接矩形；
     * 3. 将1获取的矩形与2获取的矩形面积进行比较，得到最终的面积最小外接矩形；
     *
     * @param geometry
     * @param slide
     * @param annotation
     * @return List<Annotation>
     */
    public List<Annotation> list(Geometry geometry, Slide slide, Annotation annotation, int distance) {

        List<Annotation> list = new ArrayList<>();

        // log.info("Geo:{}",annotation.getLocation());
        // geometry.getInteriorPoint(); // 取内部点
        // 左上: 6912.466019417474   -1156.9708737864078
        // 右下：24190.912621359224 -17263.223300970873

        geometry = geometry.getEnvelope();

        // 左上角
        double xLT = geometry.getCoordinates()[0].x < geometry.getCoordinates()[2].x ? geometry.getCoordinates()[0].x : geometry.getCoordinates()[2].x;
        double yLT = Math.abs(geometry.getCoordinates()[0].y) < Math.abs(geometry.getCoordinates()[2].y) ? Math.abs(geometry.getCoordinates()[0].y) : Math.abs(geometry.getCoordinates()[2].y);

        // 右下角
        double xRB = geometry.getCoordinates()[2].x > geometry.getCoordinates()[0].x ? geometry.getCoordinates()[2].x : geometry.getCoordinates()[0].x;
        double yRB = Math.abs(geometry.getCoordinates()[2].y) > Math.abs(geometry.getCoordinates()[0].y) ? Math.abs(geometry.getCoordinates()[2].y) : Math.abs(geometry.getCoordinates()[0].y);

        // 总宽
        double disX = xRB - xLT;
        // 总高
        double disY = yRB - yLT;

//        log.info("--------------------disX:{},disY:{}",disX,disY);

        // 横向个数（列数）
        int cXdistance = (int) disX / distance;
        // 纵向个数（行数）
        int cYdistance = (int) disY / distance;

//        log.info("--------------------disX/distance:{},disY/distance:{}",cXdistance,cYdistance);
//        log.info("--------------------disX%distance:{},disY%distance:{}",disX%distance,disY%distance);

        // 大于distance一半加1
        if (disX % distance >= distance / 2) {
            cXdistance++;
        }

        // 大于distance一半加1
        if (disY % distance >= distance / 2) {
            cYdistance++;
        }

        // 极小值置1
        if (cXdistance == 0) {
            cXdistance = 1;
        }

        // 极小值置1
        if (cYdistance == 0) {
            cYdistance = 1;
        }


        // 横向个数（列数）
        for (int x = 0; x < cXdistance; x++) {
            // 纵向个数（行数）
            for (int y = 0; y < cYdistance; y++) {
//                log.info("--------------------x:{},y:{}",x,y);

                // 竖线
                Annotation stAnnotation = new Annotation();
                stAnnotation.setLocation("LINESTRING(" + (xLT + distance * x) + " " + (yLT + distance * y) * (-1) + "," + (xLT + distance * x) + " " + (yLT + distance * (y + 1)) * (-1) + ")");
                stAnnotation.setSlideId(slide.getSlideId());
                stAnnotation.setLocationType("LineString");
                stAnnotation.setMeasure("0");
                stAnnotation.setPerimeter(String.valueOf(distance));
                stAnnotation.setCreateBy(0L);
                stAnnotation.setImageId(slide.getImageId());
                stAnnotation.setProjectId(slide.getProjectId());
                stAnnotation.setCategoryId(0L);
                // 添加更新人和更新时间
                stAnnotation.setUpdateBy(0L);
                list.add(stAnnotation);

                // 横线
                Annotation stAnnotation2 = new Annotation();
                stAnnotation2.setLocation("LINESTRING(" + (xLT + distance * x) + " " + (yLT + distance * y) * (-1) + "," + (xLT + distance * (x + 1)) + " " + (yLT + distance * y) * (-1) + ")");
                stAnnotation2.setSlideId(slide.getSlideId());
                stAnnotation2.setLocationType("LineString");
                stAnnotation2.setMeasure("0");
                stAnnotation2.setPerimeter(String.valueOf(distance));
                stAnnotation2.setCreateBy(0L);
                stAnnotation2.setImageId(slide.getImageId());
                stAnnotation2.setProjectId(slide.getProjectId());
                stAnnotation2.setCategoryId(annotation.getCategoryId());
                // 添加更新人和更新时间
                stAnnotation2.setUpdateBy(0L);
                list.add(stAnnotation2);

                // 竖线
                if (x == cXdistance - 1) {
                    Annotation stAnnotation4 = new Annotation();
                    stAnnotation4.setLocation("LINESTRING(" + (xLT + distance * (x + 1)) + " " + (yLT + distance * (y)) * (-1) + "," + (xLT + distance * (x + 1)) + " " + (yLT + distance * (y + 1)) * (-1) + ")");
                    stAnnotation4.setSlideId(slide.getSlideId());
                    stAnnotation4.setLocationType("LineString");
                    stAnnotation4.setMeasure("0");
                    stAnnotation4.setPerimeter(String.valueOf(distance));
                    stAnnotation4.setCreateBy(0L);
                    stAnnotation4.setImageId(slide.getImageId());
                    stAnnotation4.setProjectId(slide.getProjectId());
                    stAnnotation4.setCategoryId(annotation.getCategoryId());
                    // 添加更新人和更新时间
                    stAnnotation4.setUpdateBy(0L);
                    list.add(stAnnotation4);
                }

                // 横线
                if (y == cYdistance - 1) {

                    Annotation stAnnotation3 = new Annotation();
                    stAnnotation3.setLocation("LINESTRING(" + (xLT + distance * x) + " " + (yLT + distance * (y + 1)) * (-1) + "," + (xLT + distance * (x + 1)) + " " + (yLT + distance * (y + 1)) * (-1) + ")");
                    stAnnotation3.setSlideId(slide.getSlideId());
                    stAnnotation3.setLocationType("LineString");
                    stAnnotation3.setMeasure("0");
                    stAnnotation3.setPerimeter(String.valueOf(distance));
                    stAnnotation3.setCreateBy(0L);
                    stAnnotation3.setImageId(slide.getImageId());
                    stAnnotation3.setProjectId(slide.getProjectId());
                    stAnnotation3.setCategoryId(annotation.getCategoryId());
                    // 添加更新人和更新时间
                    stAnnotation3.setUpdateBy(0L);
                    list.add(stAnnotation3);
                }

            }
        }

        return list;
    }

}





