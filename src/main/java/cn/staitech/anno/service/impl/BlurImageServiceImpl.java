package cn.staitech.anno.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.domain.BlurImage;
import cn.staitech.anno.mapper.BlurImageMapper;
import cn.staitech.anno.service.BlurImageService;
import cn.staitech.anno.utils.DateUtils;
import cn.staitech.anno.vo.blurimage.in.ImageVagueQueryIn;
import cn.staitech.anno.vo.blurimage.out.ImageVagueListOutVO;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.utils.bean.BeanUtils;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wmy
 * @since 2024-04-10
 */
@Service
public class BlurImageServiceImpl extends ServiceImpl<BlurImageMapper, BlurImage> implements BlurImageService {

    @Override
    public PageResponse<ImageVagueListOutVO> getImageVagueList(ImageVagueQueryIn req) throws ParseException {
        Page<BlurImage> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        PageResponse<ImageVagueListOutVO> resp = new PageResponse<>();
        LambdaQueryWrapper<BlurImage> wrapper = new LambdaQueryWrapper<>();
        if (StringUtil.isNotBlank(req.getImageName())) {
            wrapper.like(BlurImage::getImageName, req.getImageName());
        }
        if (StringUtil.isNotBlank(req.getTopicName())) {
            wrapper.like(BlurImage::getTopicName, req.getTopicName());
        }
        if (ObjectUtil.isNotEmpty(req.getOrganizationId())) {
            wrapper.eq(BlurImage::getOrganizationId, req.getOrganizationId());
        }
        if (ObjectUtil.isNotEmpty(req.getStripFuzzy())) {
            wrapper.eq(BlurImage::getStripFuzzy, req.getStripFuzzy());
        }
        if (ObjectUtil.isNotEmpty(req.getMultipleFuzzy())) {
            wrapper.eq(BlurImage::getMultipleFuzzy, req.getMultipleFuzzy());
        }
        if (ObjectUtil.isNotEmpty(req.getDefinitionStatus())) {
            wrapper.eq(BlurImage::getDefinitionStatus, req.getDefinitionStatus());
        }
        if (ObjectUtil.isNotEmpty(req.getCreateTimeParams())) {
            Date date = DateUtils.addAndSubtractDaysByString(req.getCreateTimeParams().get("endTime"), 1);
            wrapper.lt(BlurImage::getCreateTime, date);
            wrapper.ge(BlurImage::getCreateTime, DateUtils.stringToDate(req.getCreateTimeParams().get("beginTime"), "yyyy-MM-dd"));
        }
        List<BlurImage> list = getBaseMapper().selectList(wrapper);
        List<ImageVagueListOutVO> outVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            outVOS = list.stream().map(image -> {
                ImageVagueListOutVO outVO = new ImageVagueListOutVO();
                BeanUtils.copyProperties(image, outVO);
                outVO.setFuzzyProportion(image.getFuzzyChunk() + "/" + image.getFuzzyCountChunk());
                outVO.setImageId(image.getImageId().longValue());
                outVO.setTopicId(image.getTopicId().longValue());
                outVO.setOrganizationId(image.getOrganizationId().longValue());
                outVO.setOrganizationName(MapConstant.getOrganizationName(outVO.getOrganizationId()));
                return outVO;
            }).collect(Collectors.toList());
        }
        resp.setList(outVOS);
        resp.setTotal(page.getTotal());
        resp.setPages(page.getPages());
        resp.setPageNum(req.getPageNum());
        resp.setPageSize(req.getPageSize());
        return resp;
    }

}
