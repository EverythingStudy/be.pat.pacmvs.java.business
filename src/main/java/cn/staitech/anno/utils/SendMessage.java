package cn.staitech.anno.utils;

import cn.staitech.anno.vo.annotation.AnnoBroadcastVO;
import cn.staitech.anno.vo.annotation.AnnoFeatures;
import cn.staitech.anno.vo.annotation.AnnotationBroadcastVO;
import cn.staitech.anno.vo.annotation.BroadcastVO;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.marking.PointCount;

import java.util.ArrayList;
import java.util.List;

/**
 * socket发送内容
 */
@SuppressWarnings({"checkstyle:TypeName", "checkstyle:EmptyLineSeparator"})
public class SendMessage {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static BroadcastVO sendOneMessages(String status, Features features, List<PointCount> pointCountRes) {
        BroadcastVO broadcast = new BroadcastVO();
        broadcast.setData(features);
        broadcast.setType(status);
        broadcast.setPoint_count_list(pointCountRes);
        return broadcast;

    }

    public static BroadcastVO sendListMessages(String annoType, String status, Features features, List<PointCount> pointCountRes) {
        BroadcastVO broadcast = new BroadcastVO();
        broadcast.setData(features);
        broadcast.setType(status);
        broadcast.setAnnotation_type(annoType);
        broadcast.setPoint_count_list(pointCountRes);
        return broadcast;

    }

    public static BroadcastVO sendOneMessages(String status, Features features) {
        BroadcastVO broadcast = new BroadcastVO();
        broadcast.setData(features);
        broadcast.setType(status);
        broadcast.setPoint_count_list(new ArrayList<>());
        return broadcast;
    }

    public static BroadcastVO sendOneMessagesByAnnoType(String annoType, String status, Features features) {
        BroadcastVO broadcast = new BroadcastVO();
        broadcast.setData(features);
        broadcast.setType(status);
        broadcast.setAnnotation_type(annoType);
        broadcast.setPoint_count_list(new ArrayList<>());
        return broadcast;
    }

    public static BroadcastVO sendPointCountMessages(String status, List<PointCount> pointCount) {
        BroadcastVO broadcast = new BroadcastVO();
        broadcast.setPoint_count_list(pointCount);
        broadcast.setType(status);
        return broadcast;

    }

    public static BroadcastVO sendAllMessages(Integer code, String message, String status,
                                              List<AnnotationBroadcastVO> annotationBroadcastVo) {
        BroadcastVO broadcast = new BroadcastVO();
        return broadcast;
    }

    public static AnnoBroadcastVO sendAnnoMessages(String status, AnnoFeatures features, List<PointCount> pointCountRes) {
        AnnoBroadcastVO broadcast = new AnnoBroadcastVO();
        broadcast.setData(features);
        broadcast.setType(status);
        broadcast.setPoint_count_list(pointCountRes);
        return broadcast;
    }
}
