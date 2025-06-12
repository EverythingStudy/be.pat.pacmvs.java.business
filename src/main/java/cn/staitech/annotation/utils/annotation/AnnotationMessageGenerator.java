package cn.staitech.annotation.utils.annotation;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.staitech.common.core.domain.R;
import cn.staitech.annotation.constant.Constant;
import cn.staitech.annotation.domain.Annotation;
import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.annotation.netty.message.AnnotationMessage;
import cn.staitech.annotation.netty.message.AnnotationProperties;
import cn.staitech.system.api.RemoteBizService;
import cn.staitech.system.api.RemoteUserService;
import cn.staitech.system.api.domain.SysUser;
import cn.staitech.system.api.domain.biz.StructureTagPageQuery;
import cn.staitech.system.api.domain.biz.StructureTagPageVo;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2025/5/22 13:53:05
 */
public class AnnotationMessageGenerator {

    public static AnnotationMessage generateAnnotationMessage(Annotation annotation, String action){
        AnnotationProperties properties = generateProperties(annotation);
        AnnotationFeature feature = generateFeatures(annotation, properties);
        AnnotationMessage annotationMessage = new AnnotationMessage();
        annotationMessage.setType(action);
        annotationMessage.setAnnotation_type(Constant.ANNO_TYPE_DRAW);
        annotationMessage.setSlideId(annotation.getSlideId());
        annotationMessage.setData(feature);
        return annotationMessage;
    }

    public static AnnotationFeature generateFeatures(Annotation annotation){

        AnnotationFeature feature = new AnnotationFeature();
        feature.setGeometry(annotation.getGeometry());
        feature.setId(annotation.getJsonId());
        feature.setProperties(generateProperties(annotation));
        return feature;
    }

    public static List<AnnotationFeature> generateFeatures(List<Annotation> annotations){
        Map<Long,StructureTagPageVo> tagMap = getTagMap(annotations);
        Map<Long, SysUser> userMap = getUserMap();
        List<AnnotationFeature> features = CollectionUtils.isEmpty(annotations) ? new ArrayList<>() : annotations.stream().map(annotation -> {
            AnnotationFeature feature = generateFeatures(annotation, generateProperties(annotation, tagMap, userMap));
            return feature;
        }).collect(Collectors.toList());
        return features;
    }

    public static AnnotationFeature generateFeatures(Annotation annotation, AnnotationProperties properties){

        AnnotationFeature feature = new AnnotationFeature();
        feature.setGeometry(annotation.getGeometry());
        feature.setId(annotation.getJsonId());
        feature.setProperties(properties);
        return feature;
    }


    /**
     * 根据 Annotation 生成 PropertiesBriefly 对象
     *
     * @param annotation 注解对象
     * @return PropertiesBriefly 属性信息
     */
    public static AnnotationProperties generateProperties(Annotation annotation) {
        return generateProperties(Arrays.asList(annotation)).get(0);
    }

    public static AnnotationProperties generateProperties(Annotation annotation,Map<Long,StructureTagPageVo> tagMap,Map<Long, SysUser> userMap) {
        return generateProperties(Arrays.asList(annotation),tagMap,userMap).get(0);
    }

    public static List<AnnotationProperties> generateProperties(List<Annotation> annotations) {

        Map<Long,StructureTagPageVo> tagMap = getTagMap(annotations);
        Map<Long, SysUser> userMap = getUserMap();
        return generateProperties(annotations,tagMap,userMap);
    }

    public static List<AnnotationProperties> generateProperties(List<Annotation> annotations,Map<Long,StructureTagPageVo> tagMap,Map<Long, SysUser> userMap) {

        List<AnnotationProperties> result = CollectionUtils.isEmpty(annotations) ? new ArrayList<>() : annotations.stream().map(annotation -> {
            AnnotationProperties properties = new AnnotationProperties();
            properties.setA0(String.valueOf(annotation.getAnnotationId()));
            properties.setA1(annotation.getLocationType());
            properties.setA2(annotation.getAnnotationType());
            properties.setA3(annotation.getTagId());
            properties.setA6(formatBigDecimal(annotation.getPerimeter()));
            properties.setA7(formatBigDecimal(annotation.getArea()));
            properties.setA8(annotation.getDescription());
            properties.setA11(annotation.getCreateBy());
            properties.setA12(DateUtil.format(annotation.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
            properties.setA13(annotation.getUpdateBy());
            setTagInfo(properties, tagMap.get(annotation.getTagId()));
            setUserInfo(properties, annotation.getCreateBy(), userMap, true);
            setUserInfo(properties, annotation.getUpdateBy(), userMap, false);
            return properties;
        }).collect(Collectors.toList());
        return result;
    }

    public static String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "0.000";
        }
        DecimalFormat df = new DecimalFormat("#,##0.000");
        return df.format(value);
    }

    /**
     * 设置分类信息
     * @param properties
     * @param tag
     */
    private static void setTagInfo(AnnotationProperties properties, StructureTagPageVo tag) {
        if (tag != null) {
            properties.setA4(tag.getRgb());
            properties.setA5(tag.getStructureTagName());
        }
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

    /**
     * 根据 Annotation 列表生成标签字典
     * @param annotations
     * @return
     */
    private static  Map<Long,StructureTagPageVo> getTagMap(List<Annotation> annotations){
        Map<Long,StructureTagPageVo> tagMap = new HashMap<>();
        try {
            List<Long> tagIds = CollectionUtils.isEmpty(annotations) ? new ArrayList<>() : annotations.stream()
                    .map(Annotation::getTagId)
                    .collect(Collectors.toList());
            StructureTagPageQuery query = new StructureTagPageQuery();
            query.setStructureTagIds(tagIds);
            RemoteBizService remoteBizService = SpringUtil.getBean(RemoteBizService.class);
            R<List<StructureTagPageVo>> tagResp = remoteBizService.queryTag(query);
            if (tagResp.getCode() == 200){
                List<StructureTagPageVo> tags = tagResp == null ? null : tagResp.getData();
                tagMap = tags == null ? new HashMap<>() : tags.stream()
                        .collect(Collectors.toMap(
                                StructureTagPageVo::getStructureTagId,
                                tag -> tag,
                                (existing, replacement) -> existing // 遇到重复 key 保留第一个
                        ));
            }else {
                throw new RuntimeException(tagResp.getMsg());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tagMap;
    }

    /**
     * 获取用户字典
     * @return
     */
    private static Map<Long, SysUser> getUserMap() {
        RemoteUserService remoteUserService = SpringUtil.getBean(RemoteUserService.class);
        R<List<SysUser>> r = remoteUserService.query(new SysUser());
        List<SysUser> users = r == null ? null : r.getData();
        Map<Long, SysUser> userMap = users == null ? new HashMap<>() : users.stream()
                .filter(user -> user.getUserId() != null)
                .collect(Collectors.toMap(
                        SysUser::getUserId,
                        user -> user,
                        (existing, replacement) -> existing // 遇到重复 key 保留第一个
                ));
        return userMap;
    }
}

