package cn.staitech.anno.service.impl;

import cn.staitech.anno.config.MapConstant;
import cn.staitech.anno.constant.Container;
import cn.staitech.anno.constant.DataConstants;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.image.Image;
import cn.staitech.anno.domain.image.ImageStatus;
import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageListFindIn;
import cn.staitech.anno.domain.image.in.ImageUpdateVO;
import cn.staitech.anno.domain.image.out.ImageListFindOut;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.utils.DateUtils;
import cn.staitech.anno.utils.LanguageUtils;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 94024
 * @description 针对表【tb_image】的数据库操作Service实现
 * @createDate 2024-09-10 10:21:48
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
        implements ImageService {
    @Resource
    private SlideService slideService;
    @Resource
    private SlideMapper slideMapper;
    @Resource
    private ImageMapper imageMapper;

    /**
     * 切片状态列表 .
     */
    @Override
    public List<ImageStatus> status() {
        List<ImageStatus> list = new ArrayList<>();
        if (LanguageUtils.isEn()) {
            for (Map.Entry<Integer, String> entry : Container.IMAGE_STATUS_MAP_EN.entrySet()) {
                list.add(new ImageStatus(entry.getKey(), entry.getValue()));
            }
        } else {
            for (Map.Entry<Integer, String> entry : Container.IMAGE_STATUS_MAP.entrySet()) {
                list.add(new ImageStatus(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    /**
     * 根据提供的查询条件，获取分页的图片列表
     *
     * @throws ParseException 如果在解析时间字符串时发生错误
     */
    @Override
    public PageResponse<ImageListFindOut> findImageList(ImageListFindIn findIn) throws ParseException {
        PageResponse<ImageListFindOut> response = new PageResponse<>();

        // 分页查询设置
        Page<ImageListFindOut> page = PageHelper.startPage(findIn.getPageNum(), findIn.getPageSize());

        // 获取当前登录用户的信息
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();

        // 非管理员用户限制查询条件
        if (!DataConstants.ADMIN.equals(sysUser.getUserName())) {
            findIn.setOrganizationId(sysUser.getOrganizationId());
        }

        // 构建查询条件
        LambdaQueryWrapper<Image> wrapper = buildQueryWrapper(findIn);

        // 执行查询
        List<Image> images = this.baseMapper.selectList(wrapper);

        // 转换为输出对象列表
        List<ImageListFindOut> findOuts = images.stream().map(this::convertToFindOut)
                .collect(Collectors.toList());

        response.setList(findOuts);
        response.setTotal(page.getTotal());
        response.setPages(page.getPages());
        response.setPageNum(findIn.getPageNum());
        response.setPageSize(findIn.getPageSize());

        return response;
    }

    /***
     * 批量删除图片（物理删除）
     * 1、若符合删除条件，图像物理删除；
     * 2、与切片列表有关联的不能删除；
     * @param ids 图像ids
     * @return 不可删除的图片ID列表
     */
    @Override
    public List<Long> deleteBatchIds(ImageBatchIdsVO ids) {

        // 不可删除的列表
        List<Long> forbidIds = new ArrayList<>();

        for (Long imageId : ids.getImageIdList()) {
            // 查询fr_slide是否有关系图像
            Integer frSlideCount = imageMapper.selectFrSlideCountByImageId(imageId);

            if (frSlideCount > 0) {
                forbidIds.add(imageId);
            } else {
                imageMapper.deleteById(imageId);
            }
        }

        return forbidIds;
    }

    /**
     * 关联切片与专题、组织ID，没有专题则新添加
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(ImageUpdateVO vo) {
        Image image = new Image();
        BeanUtils.copyProperties(vo, image);
        image.setFileName(StringUtils.substringBeforeLast(vo.getImageName(), "."));

        // 获取当前登录用户Id
        Long loginUser = SecurityUtils.getUserId();
        image.setUpdateBy(loginUser);
        return imageMapper.updateById(image);
    }

    /**
     * 构建查询条件的包装器
     *
     * @param findIn 查询条件输入对象
     * @return 配置好的LambdaQueryWrapper对象
     */
    private LambdaQueryWrapper<Image> buildQueryWrapper(ImageListFindIn findIn) throws ParseException {
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(findIn.getImageName())) {
            wrapper.eq(Image::getImageName, findIn.getImageName());
        }
        if (StringUtils.isNotEmpty(findIn.getTopicName())) {
            wrapper.eq(Image::getTopicName, findIn.getTopicName());
        }
        if (findIn.getOrganizationId() != null) {
            wrapper.eq(Image::getOrganizationId, findIn.getOrganizationId());
        }
        if (findIn.getStatus() != null) {
            wrapper.eq(Image::getStatus, findIn.getStatus());
        }
        if (findIn.getCreateTime() != null && !findIn.getCreateTime().isEmpty()) {
            String beginTime = findIn.getCreateTime().get(DataConstants.BEGIN_TIME);
            String endTime = findIn.getCreateTime().get(DataConstants.END_TIME);
            if (StringUtils.isNotEmpty(beginTime)) {
                wrapper.ge(Image::getCreateTime, DateUtils.stringToDate(beginTime, DataConstants.TIME_FORMAT));
            }
            if (StringUtils.isNotEmpty(endTime)) {
                wrapper.le(Image::getCreateTime, DateUtils.addAndSubtractDaysByString(endTime, 1));
            }
        }
        wrapper.orderByDesc(Image::getImageId);
        return wrapper;
    }

    /**
     * 将Image对象转换为ImageListFindOut对象
     *
     * @param image Image对象
     * @return 转换后的ImageListFindOut对象
     */
    private ImageListFindOut convertToFindOut(Image image) {
        ImageListFindOut out = new ImageListFindOut();
        BeanUtils.copyProperties(image, out);

        // 根据语言设置文件状态的显示名称
        String fileStatus = LanguageUtils.isEn()
                ? Container.IMAGE_STATUS_MAP_EN.get(image.getStatus())
                : Container.IMAGE_STATUS_MAP.get(image.getStatus());
        out.setFileStatus(fileStatus);

        if (null != image.getAnalyzeStatus()) {
            String analyzeStatus = Objects.equals(DataConstants.NUMBER_0, image.getAnalyzeStatus()) ? "失败" : "成功";
            out.setAnalyzeStatusName(analyzeStatus);
        }

        // 匹配并设置机构名称
        out.setOrganizationName(MapConstant.getOrganizationName(image.getOrganizationId()));

        // 设置删除状态
        Slide slide = new Slide();
        slide.setImageId(out.getImageId());
        out.setDeleState(imageMapper.selectFrSlideCountByImageId(out.getImageId()) > 0 ? DataConstants.NUMBER_1 : DataConstants.NUMBER_0);

        return out;
    }
}




