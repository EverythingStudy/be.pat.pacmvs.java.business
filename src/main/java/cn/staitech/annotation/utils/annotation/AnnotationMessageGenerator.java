package cn.staitech.annotation.utils.annotation;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.staitech.annotation.netty.message.AnnotationSdFeature;
import cn.staitech.annotation.vo.anno.AnnotationSdVo;
import cn.staitech.common.core.domain.R;
import cn.staitech.annotation.constant.Constant;
import cn.staitech.annotation.domain.Annotation;
import cn.staitech.annotation.netty.message.AnnotationFeature;
import cn.staitech.annotation.netty.message.AnnotationMessage;
import cn.staitech.annotation.netty.message.AnnotationProperties;
import cn.staitech.sft.logaudit.LogAuditAop;
import cn.staitech.system.api.RemoteBizService;
import cn.staitech.system.api.RemoteUserService;
import cn.staitech.system.api.domain.SysUser;
import cn.staitech.system.api.domain.biz.OrganTagQuery;
import cn.staitech.system.api.domain.biz.OrganTagQueryVo;
import cn.staitech.system.api.domain.biz.StructureTagPageQuery;
import cn.staitech.system.api.domain.biz.StructureTagPageVo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AnnotationMessageGenerator {

    public static AnnotationMessage generateAnnotationMessage(Annotation annotation, String action){
        AnnotationProperties properties = generateProperties(annotation);
        AnnotationFeature feature = generateFeatures(annotation, properties);
        SpringUtil.getBean(LogAuditAop.class).setEncrypt(feature, true);
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

    public static List<AnnotationFeature> generateFeatures(List<Annotation> annotations,Integer contourType) {
        List<AnnotationFeature> features = null;
        Map<Long, SysUser> userMap = getUserMap();
        // 粗轮廓：查询脏器标签
        if (contourType != null && contourType == 1) {
            Map<Long, OrganTagQueryVo> tagMap = new HashMap<>(16);
            if (!CollectionUtils.isEmpty(annotations)) {
                List<Long> organTagIds = annotations.stream().map(Annotation::getTagId).collect(Collectors.toList());
                OrganTagQuery organTagQuery = new OrganTagQuery();
                organTagQuery.setOrganTagIds(organTagIds);
                tagMap = queryOrganTag(organTagQuery);
            }
            Map<Long, OrganTagQueryVo> finalTagMap = tagMap;
            features = CollectionUtils.isEmpty(annotations) ? new ArrayList<>() : annotations.stream().map(annotation -> {
                AnnotationFeature feature = generateFeatures(annotation, generatePropertiesForOrgan(annotation, finalTagMap, userMap));
                return feature;
            }).collect(Collectors.toList());
        }
        // 其他：默认查询结构标签
        else {
            Map<Long, StructureTagPageVo> tagMap = getTagMap(annotations);
            features = CollectionUtils.isEmpty(annotations) ? new ArrayList<>() : annotations.stream().map(annotation -> {
                AnnotationFeature feature = generateFeatures(annotation, generateProperties(annotation, tagMap, userMap));
                return feature;
            }).collect(Collectors.toList());
        }
        return features;
    }

    private static AnnotationProperties generatePropertiesForOrgan(Annotation annotation, Map<Long, OrganTagQueryVo> tagMap, Map<Long, SysUser> userMap) {
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

        OrganTagQueryVo tag = tagMap.get(annotation.getTagId());
        if (tag != null) {
            properties.setA4(tag.getRgb());
            properties.setA5(tag.getOrganName());
        }

        setUserInfo(properties, annotation.getCreateBy(), userMap, true);
        setUserInfo(properties, annotation.getUpdateBy(), userMap, false);
        return properties;
    }

    private static Map<Long, OrganTagQueryVo> queryOrganTag(OrganTagQuery organTagQuery) {
        Map<Long, OrganTagQueryVo> map = new HashMap<>(16);
        try {
            RemoteBizService remoteBizService = SpringUtil.getBean(RemoteBizService.class);
            R<List<OrganTagQueryVo>> result = remoteBizService.queryOrganTag(organTagQuery);
            if (result != null && !CollectionUtils.isEmpty(result.getData())) {
                map = result.getData().stream().collect(Collectors.toMap(OrganTagQueryVo::getOrganTagId, organTagQueryVo -> organTagQueryVo, (existing, replacement) -> existing));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static AnnotationFeature generateFeatures(Annotation annotation, AnnotationProperties properties){

        AnnotationFeature feature = new AnnotationFeature();
        feature.setDescription(annotation.getDescription());
        feature.setTagIdLog(annotation.getTagIdLog());
        feature.setGeometry(annotation.getGeometry());
        feature.setId(annotation.getJsonId());
        feature.setProperties(properties);
        return feature;
    }

    public static AnnotationSdFeature generateSdFeatures(AnnotationSdVo annotation, AnnotationProperties properties) {
        AnnotationSdFeature feature = new AnnotationSdFeature();
        feature.setGeometry(JSONObject.parseObject(annotation.getContour()));
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
        Map<Long, SysUser> userMap = getUserMap();
        // 脏器标签
        if (!CollectionUtils.isEmpty(annotations) && annotations.get(0).getContourType() != null && annotations.get(0).getContourType() == 1) {
            List<Long> organTagIds = annotations.stream().map(Annotation::getTagId).collect(Collectors.toList());
            OrganTagQuery organTagQuery = new OrganTagQuery();
            organTagQuery.setOrganTagIds(organTagIds);
            Map<Long, OrganTagQueryVo> tagMap = queryOrganTag(organTagQuery);

            List<AnnotationProperties> properties = new ArrayList<>();
            for (Annotation annotation : annotations) {
                properties.add(generatePropertiesForOrgan(annotation, tagMap, userMap));
            }
            return properties;
        } else {
            Map<Long,StructureTagPageVo> tagMap = getTagMap(annotations);
            return generateProperties(annotations, tagMap, userMap);
        }
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
            log.info("查询结构标签信息参数：{}", JSONObject.toJSONString(query));
            R<List<StructureTagPageVo>> tagResp = remoteBizService.queryTag(query);
            log.info("查询结构标签信息返回：{}", JSONObject.toJSONString(tagResp));
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
     * 根据 Annotation 列表生成标签字典
     *
     * @param annotations
     * @return
     */
    private static Map<Long, StructureTagPageVo> getSdTagMap(List<AnnotationSdVo> annotations) {
        Map<Long, StructureTagPageVo> tagMap = new HashMap<>();
        try {
            Set<Long> tagIds = CollectionUtils.isEmpty(annotations) ? new HashSet<>() : annotations.stream()
                    .map(Annotation::getTagId)
                    .collect(Collectors.toSet());
            StructureTagPageQuery query = new StructureTagPageQuery();
            query.setStructureTagIds(new ArrayList<>(tagIds));
            RemoteBizService remoteBizService = SpringUtil.getBean(RemoteBizService.class);
            log.info("查询结构标签信息参数：{}", JSONObject.toJSONString(query));
            R<List<StructureTagPageVo>> tagResp = remoteBizService.queryTag(query);
            log.info("查询结构标签信息返回：{}", JSONObject.toJSONString(tagResp));
            if (tagResp.getCode() == 200) {
                List<StructureTagPageVo> tags = tagResp == null ? null : tagResp.getData();
                tagMap = tags == null ? new HashMap<>() : tags.stream()
                        .collect(Collectors.toMap(
                                StructureTagPageVo::getStructureTagId,
                                tag -> tag,
                                (existing, replacement) -> existing // 遇到重复 key 保留第一个
                        ));
            } else {
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

    /**
     * 获取筛差数据
     */
    public static List<AnnotationSdFeature> generateFeatures(List<AnnotationSdVo> annotations) {
        List<AnnotationSdFeature> features = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(annotations)) {
            // 获取用户字典
            Map<Long, SysUser> userMap = getUserMap();
            // 查询结构标签
            Map<Long, StructureTagPageVo> tagMap = getSdTagMap(annotations);
            features = annotations.stream().map(annotation -> {
                AnnotationSdFeature feature = generateSdFeatures(annotation, generateProperties(annotation, tagMap, userMap));
                return feature;
            }).collect(Collectors.toList());
        }
        return features;
    }
}

