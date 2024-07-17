package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.ContourRoi;
import cn.staitech.anno.mapper.ContourRoiMapper;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.project.domain.Slide;
import cn.staitech.anno.project.domain.SysUser;
import cn.staitech.anno.project.mapper.PathologicalIndicatorCategoryMapperV1;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.project.mapper.SysUserMapperV1;
import cn.staitech.anno.service.ContourRoiService;
import cn.staitech.anno.utils.MarkingUtils;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.SendMessage;
import cn.staitech.anno.vo.annotation.BroadcastVO;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.geojson.in.ViewAddIn;
import cn.staitech.common.redis.service.RedisService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.staitech.anno.constant.CommonConstant.ADD_STATUS;
import static cn.staitech.anno.constant.CommonConstant.DELETE_STATUS;

/**
 * @author admin
 * @description 针对表【tb_contour_roi】的数据库操作Service实现
 * @createDate 2024-06-11 13:22:07
 */
@Slf4j
@Service
public class ContourRoiServiceImpl extends ServiceImpl<ContourRoiMapper, ContourRoi>
        implements ContourRoiService {
    @Resource
    private RedisService redisService;
    @Resource
    private SlideMapperV1 slideMapperV1;
    @Resource
    private ContourRoiMapper contourRoiMapper;

    @Resource
    private SysUserMapperV1 userMapper;

    ExpiringMap<Long, PathologicalIndicatorCategory> pathologicalIndicatorCategoryHashMap = ExpiringMap.builder().maxSize(1000).expiration(12, TimeUnit.HOURS).variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();
    ExpiringMap<Long, SysUser> userMap = ExpiringMap.builder().maxSize(1000).expiration(12, TimeUnit.HOURS).variableExpiration().expirationPolicy(ExpirationPolicy.CREATED).build();


    @Resource
    private PathologicalIndicatorCategoryMapperV1 pathologicalIndicatorCategoryMapperV1;
    @Override
    public List<Features> selectList(Long slideId) throws Exception {
        List<Features> list = new ArrayList<>();
        Slide slideBy = redisService.getCacheObject(CommonConstant.ANNO_SLIDE + slideId);
        if (null == slideBy) {
            slideBy = slideMapperV1.selectById(slideId);
            redisService.setCacheObject(CommonConstant.ANNO_SLIDE + slideId, slideBy, CommonConstant.SLIDE_CACHE_HOURS, TimeUnit.HOURS);
        }
        if (!Optional.ofNullable(slideBy).isPresent()) {
            throw new Exception(MessageSource.M("SLIDE_ABNORMAL_NO_INFORMATION"));
        }
        LambdaQueryWrapper<ContourRoi> map = new LambdaQueryWrapper<ContourRoi>().eq(ContourRoi::getSlideId, slideId);
        List<ContourRoi> contourRoiList = contourRoiMapper.selectList(map);
        List<Features> selfAnnoList = getFeaturesList(contourRoiList);
        if (CollectionUtils.isNotEmpty(selfAnnoList)) {
            list.addAll(selfAnnoList);
        }
        return list;
    }





    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(ViewAddIn req) throws Exception {
        if (req.getSlide_id() == null) {
            throw new Exception(MessageSource.M("MarkingDelIn.slideId.notNull"));
        }
        if (req.getGeometry() != null && !req.getGeometry().isEmpty()) {
            MarkingUtils.addVerify(req.getGeometry());
        } else {
            log.info("标注数据异常:" + req.getGeometry() + "------------------------------------------------->");
            throw new Exception("插入失败，轮廓数据不能为空");
        }
        Slide slideBy = redisService.getCacheObject(CommonConstant.ANNO_SLIDE + req.getSlide_id());
        if (null == slideBy) {
            slideBy = slideMapperV1.selectById(req.getSlide_id());
            redisService.setCacheObject(CommonConstant.ANNO_SLIDE + req.getSlide_id(), slideBy, CommonConstant.SLIDE_CACHE_HOURS, TimeUnit.HOURS);
        }
        if (slideBy == null) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        ContourRoi contourRoi = transContourRoi(req);
        contourRoi.setAnnotationType("Draw");
        contourRoi.setCreateTime(new Date());
        contourRoiMapper.insert(contourRoi);
        // 查询详情信息
        Properties properties = getProperties(contourRoiMapper.selectById(contourRoi.getContourRoiId()));
        Features features = MarkingUtils.socketData(null, JSONObject.parseObject(contourRoi.getContour()), properties);
        BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_DRAW, ADD_STATUS, features, null);
        System.out.println(req.getSlide_id());
        System.out.println(broadcastVO);
        System.out.println("-------------------------------------->");
        NioWebSocketHandler.sendAll(req.getSlide_id(), broadcastVO);
        return contourRoi.getContourRoiId();
    }


    @Override
    public int delete(String contourId) throws Exception {
        if (!Optional.ofNullable(contourId).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        ContourRoi contourRoiBy = contourRoiMapper.selectById(contourId);
        if (!Optional.ofNullable(contourRoiBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Slide slide = slideMapperV1.selectById(contourRoiBy.getSlideId());
        if (!Optional.ofNullable(slide).isPresent()) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        Properties properties = getProperties(contourRoiMapper.selectById(contourId));
        Features features = MarkingUtils.socketData(null, JSONObject.parseObject(contourRoiBy.getContour()), properties);
        BroadcastVO broadcastVO = SendMessage.sendListMessages(CommonConstant.ANNO_TYPE_DRAW, DELETE_STATUS, features, null);
        NioWebSocketHandler.sendAll(contourRoiBy.getSlideId(), broadcastVO);
        int res = contourRoiMapper.deleteById(contourId);
        return res;
    }







    public List<Features> getFeaturesList(List<ContourRoi> contourRoiList) {
        List<Features> featuresList = new ArrayList<>();
        for (ContourRoi contourRoi : contourRoiList) {
            Features features = new Features();
            features.setGeometry(JSONObject.parseObject(contourRoi.getContour()));
            features.setId(null);
            features.setType("Feature");
            String s1 = JSONObject.toJSONString(getProperties(contourRoi), SerializerFeature.PrettyFormat);
            JSONObject jsonObject = JSONObject.parseObject(s1);
            features.setProperties(jsonObject);
            featuresList.add(features);
        }
        return featuresList;
    }



    public Properties getProperties(ContourRoi contourRoi) {
        Properties properties = new Properties();
        properties.setMarking_id(String.valueOf(contourRoi.getContourRoiId()));
        properties.setArea(contourRoi.getArea());
        properties.setPerimeter(contourRoi.getPerimeter());
        properties.setLocation_type(contourRoi.getLocationType());
        properties.setCreate_by(contourRoi.getCreateBy());
        properties.setUpdate_by(contourRoi.getUpdateBy());
        properties.setAnnotation_type(contourRoi.getAnnotationType());
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        properties.setCreate_time(sim.format(contourRoi.getCreateTime()));
        if (contourRoi.getCategoryId() != null) {
            PathologicalIndicatorCategory pathologicalIndicatorCategory = pathologicalIndicatorCategoryHashMap.get(contourRoi.getCategoryId());
            if (pathologicalIndicatorCategory == null) {
                pathologicalIndicatorCategory = pathologicalIndicatorCategoryMapperV1.selectById(contourRoi.getCategoryId());
                if (pathologicalIndicatorCategory != null) {
                    pathologicalIndicatorCategoryHashMap.put(pathologicalIndicatorCategory.getCategoryId(), pathologicalIndicatorCategory);
                }
            }
            if (pathologicalIndicatorCategory != null) {
                properties.setLabel_color(pathologicalIndicatorCategory.getRgb());
                properties.setLabel_name(pathologicalIndicatorCategory.getNumber());
            }
        }
        if (contourRoi.getCreateBy() != null) {
            SysUser createUser = userMap.get(contourRoi.getCreateBy());
            if (createUser == null) {
                createUser = userMapper.selectById(contourRoi.getCreateBy());
                if (createUser != null) {
                    userMap.put(contourRoi.getCreateBy(), createUser);
                } else {
                    userMap.put(contourRoi.getCreateBy(), new SysUser());
                }
            }
            if (createUser != null && createUser.getUserId() != null) {
                properties.setAnnotation_owner(createUser.getUserName());
            }
        }
        if (contourRoi.getUpdateBy() != null) {
            SysUser updateUser = userMap.get(contourRoi.getUpdateBy());
            if (updateUser == null) {
                updateUser = userMapper.selectById(contourRoi.getUpdateBy());
                if (updateUser != null) {
                    userMap.put(contourRoi.getUpdateBy(), updateUser);
                } else {
                    userMap.put(contourRoi.getUpdateBy(), new SysUser());
                }
            }
            if (updateUser != null && updateUser.getUserId() != null) {
                properties.setAnnotation_update_owner(updateUser.getUserName());
            }
        }
        return properties;
    }


    private ContourRoi transContourRoi(ViewAddIn view) {
        ContourRoi contourRoi = new ContourRoi();
        contourRoi.setSlideId(view.getSlide_id());
        if (null != view.getCreate_by()) {
            contourRoi.setCreateBy(view.getCreate_by());
        }
        if (StringUtils.isNotEmpty(view.getArea())) {
            contourRoi.setArea(view.getArea());
        }
        if (StringUtils.isNotEmpty(view.getPerimeter())) {
            contourRoi.setPerimeter(view.getPerimeter());
        }
        if (null != view.getCategory_id()) {
            contourRoi.setCategoryId(view.getCategory_id());
        }
        if (StringUtils.isNotEmpty(view.getLocation_type())) {
            contourRoi.setLocationType(view.getLocation_type());
        }
        if (StringUtils.isNotEmpty(view.getDescription())) {
            contourRoi.setDescription(view.getDescription());
        }
        if (null != view.getGeometry()) {
            contourRoi.setContour(String.valueOf(view.getGeometry()));
        }
        return contourRoi;
    }


}




