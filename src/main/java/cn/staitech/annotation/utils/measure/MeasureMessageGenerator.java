package cn.staitech.annotation.utils.measure;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.staitech.annotation.constant.Constant;
import cn.staitech.annotation.domain.Measure;
import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.annotation.netty.message.AnnotationMessage;
import cn.staitech.annotation.netty.message.AnnotationProperties;
import cn.staitech.common.core.domain.R;
import cn.staitech.system.api.RemoteUserService;
import cn.staitech.system.api.domain.SysUser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/22 13:53:05
 */
public class MeasureMessageGenerator {
    public static AnnotationMessage generateAnnotationMessage(Measure measure,String action){
        AnnotationProperties properties = generateProperties(measure);
        AnnotationFeature feature = generateFeatures(measure, properties);
        AnnotationMessage annotationMessage = new AnnotationMessage();
        annotationMessage.setType(action);
        annotationMessage.setAnnotation_type(Constant.ANNO_TYPE_MEASURE);
        annotationMessage.setSlideId(measure.getSlideId());
        annotationMessage.setData(feature);
        return annotationMessage;
    }

    public static AnnotationFeature generateFeatures(Measure measure){

        AnnotationFeature feature = new AnnotationFeature();
        feature.setGeometry(measure.getGeometry());
        feature.setProperties(generateProperties(measure));
        return feature;
    }

    public static AnnotationFeature generateFeatures(Measure measure, AnnotationProperties properties){

        AnnotationFeature feature = new AnnotationFeature();
        feature.setGeometry(measure.getGeometry());
        feature.setProperties(properties);
        return feature;
    }


    /**
     * 根据 Annotation 生成 PropertiesBriefly 对象
     *
     * @param measure 注解对象
     * @return PropertiesBriefly 属性信息
     */
    public static AnnotationProperties generateProperties(Measure measure) {
        RemoteUserService remoteUserService = SpringUtil.getBean(RemoteUserService.class);
        R<List<SysUser>> r = remoteUserService.query(new SysUser());
        List<SysUser> users = r == null ? null : r.getData();
        AnnotationProperties properties = new AnnotationProperties();
        properties.setA0(String.valueOf(measure.getMeasureId()));
        properties.setA7(measure.getArea());
        properties.setA6(measure.getPerimeter());
        properties.setA2(measure.getAnnotationType());
        properties.setA1(measure.getLocationType());
        properties.setA12(DateUtil.format(measure.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
        properties.setA21(measure.getMeasureName());
        properties.setA24(measure.getMeasureType());
        properties.setA23(measure.getMeasureRelation());
        properties.setA22(measure.getMeasureNumber());
        properties.setA19(measure.getMeanDistance());
        properties.setA20(measure.getMinDistance());
        properties.setA18(measure.getMaxDistance());
        properties.setA16(measure.getInnerAngle());
        properties.setA15(measure.getExteriorAngle());
        properties.setA14(measure.getCenterPoint());
        properties.setA17(measure.getRadius());
        properties.setA25(measure.getMeasureFullName());
        Map<Long, SysUser> userMap = users == null ? new HashMap<>() : users.stream()
                .filter(user -> user.getUserId() != null)
                .collect(Collectors.toMap(
                        SysUser::getUserId,
                        user -> user,
                        (existing, replacement) -> existing // 遇到重复 key 保留第一个
                ));

        setUserInfo(properties, measure.getCreateBy(), userMap, true);
        setUserInfo(properties, measure.getUpdateBy(), userMap, false);
        return properties;
    }

    public static String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "0.000";
        }
        DecimalFormat df = new DecimalFormat("#,##0.000");
        return df.format(value);
    }

    /**
     * 设置用户信息 (创建者/更新者)
     *
     * @param properties 属性对象
     * @param userId     用户 ID
     * @param isCreator  是否是创建者
     */
    private static void setUserInfo(AnnotationProperties properties, Long userId, Map<Long, SysUser> userMap, boolean isCreator) {
        if (userId == null) return;
        SysUser user = userMap.get(userId);
        if (user == null) {
            return;
        }
        if (isCreator) {
            properties.setA9(user.getUserName());
        } else {
            properties.setA10(user.getUserName());
        }
    }
}

