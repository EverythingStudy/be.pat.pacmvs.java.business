package cn.staitech.anno.project.service.impl;

import cn.staitech.anno.project.domain.Annotation;
import cn.staitech.anno.project.domain.SlideAttr;
import cn.staitech.anno.project.mapper.AnnotationMapperV1;
import cn.staitech.anno.project.service.SlideAttrService;
import cn.staitech.common.security.utils.SecurityUtils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.staitech.anno.project.mapper.SlideAttrMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 86186
 * @description 针对表【tb_slide_attr(切片属性表)】的数据库操作Service实现
 * @createDate 2023-09-14 12:47:01
 */
@Service
public class SlideAttrServiceImpl extends ServiceImpl<SlideAttrMapper, SlideAttr>
        implements SlideAttrService {

    //属性类型：1、标注人员，2、标注类别
    private static final String USER = "1";
    private static final String CATEGORY = "2";

    @Resource
    private AnnotationMapperV1 annotationMapperV1;

    @Transactional
    @Override
    public Boolean saveAnnoUsers(Long slideId, List<Long> userIds)throws Exception{
        List<SlideAttr> slideAttrs = queryAttr(slideId,CATEGORY,userIds);
        return save(slideId,USER,userIds,slideAttrs);
    }
    @Transactional
    @Override
    public Boolean removeAnnoUsers(Long slideId, List<Long> userIds)throws Exception{
        QueryWrapper<Annotation> queryWrapper = Wrappers.query();
        queryWrapper.eq("slide_id",slideId);
        queryWrapper.in("create_by",userIds);
        Integer count = annotationMapperV1.selectCount(queryWrapper);
        if (count==0){
            List<SlideAttr> slideAttrs = queryAttr(slideId,USER,userIds);
            if (slideAttrs!=null&&!slideAttrs.isEmpty()){
                int i = getBaseMapper().deleteBatchIds(slideAttrs);
            }
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean saveAnnoCategory(Long slideId, List<Long> categoryIds)throws Exception{
        List<SlideAttr> slideAttrs = queryAttr(slideId,CATEGORY,categoryIds);
        return save(slideId,CATEGORY,categoryIds,slideAttrs);
    }
    @Transactional
    @Override
    public Boolean removeAnnoCategory(Long slideId, List<Long> categoryIds)throws Exception{
        QueryWrapper<Annotation> queryWrapper = Wrappers.query();
        queryWrapper.eq("slide_id",slideId);
        queryWrapper.in("category_id",categoryIds);
        Integer count = annotationMapperV1.selectCount(queryWrapper);
        if (count==0){
            List<SlideAttr> slideAttrs = queryAttr(slideId,CATEGORY,categoryIds);
            if (slideAttrs!=null&&!slideAttrs.isEmpty()){
                int i = getBaseMapper().deleteBatchIds(slideAttrs);
            }
        }
        return true;
    }

    /**
     * 查询
     * @param slideId
     * @param attrType
     * @param attrIds
     * @return
     */
    private List<SlideAttr> queryAttr(Long slideId,String attrType, List<Long> attrIds){
        List<SlideAttr> slideAttrs = new ArrayList<>();
        if (attrIds!=null&&!attrIds.isEmpty()){
            QueryWrapper<SlideAttr> queryWrapper = Wrappers.query();
            queryWrapper.eq("slide_id",slideId);
            queryWrapper.in("attr_id",attrIds);
            queryWrapper.eq("attr_type",attrType);
            slideAttrs = getBaseMapper().selectList(queryWrapper);
        }
        return slideAttrs;
    }

    /**
     * 保存
     * @param slideId
     * @param attrType
     * @param attrIds
     * @param slideAttrs
     * @return
     * @throws Exception
     */
    private Boolean save(Long slideId,String attrType, List<Long> attrIds,List<SlideAttr> slideAttrs)throws Exception{
        List<SlideAttr> resp = new ArrayList<>();
        Long userId = SecurityUtils.getUserId();
        Map<Long,SlideAttr> map = new HashMap<>();
        if (slideAttrs!=null&&!slideAttrs.isEmpty()){
            slideAttrs.forEach(slideAttr -> {
                map.put(slideAttr.getAttrId(),slideAttr);
            });
        }
        attrIds.forEach(a->{
            if (map.get(a)==null){
                SlideAttr s = SlideAttr.builder().attrId(a).attrType(attrType).slideId(slideId).createBy(userId).createTime(new Date())
                        .updateBy(userId).updateTime(new Date()).build();
                resp.add(s);
            }
        });
        return saveBatch(resp);
    }

}




