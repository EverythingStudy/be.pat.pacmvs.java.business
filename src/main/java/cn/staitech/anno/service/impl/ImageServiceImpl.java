package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.Container;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.image.in.*;
import cn.staitech.anno.domain.image.out.ImageListOutVO;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.SpecialImageMapper;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.RoundService;
import cn.staitech.anno.service.SlideService;
import cn.staitech.anno.service.SysOrganizationService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 切片列表（原图像）服务层实现
 *
 * @author wangfeng
 * @date 2023/06/01
 */
@Slf4j
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    @Resource
    private ImageMapper imageMapper;

    @Resource
    private SlideService slideService;

    @Resource
    private SpecialImageMapper specialImageMapper;

    @Resource
    private SysOrganizationService sysOrganizationService;

    @Resource
    private RoundService roundService;

    /**
     * 切片列表（原图像）
     *
     * @param vo
     * @return
     */
    @Override
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public PageMaster<ImageListOutVO> selectList(ImageListVO vo) throws ExecutionException, InterruptedException {
        Image image = new Image();
        BeanUtils.copyProperties(vo, image);

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        image.setOrganizationId(sysUser.getOrganizationId());

        // 业务类型 1 原始切片 2 预测切片
        Integer bizType = image.getBizType();
        // 所有的轮次Map
        Map<Long, String> roundMap = null;

        // 异步查询图像列表
        CompletableFuture<PageMaster<Image>> listFuture = CompletableFuture.supplyAsync(() -> {
            // 分页
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize()).setReasonable(true);
            List<Image> list = imageMapper.selectListSlfe(image);
            PageMaster pageMaster = new PageMaster<>(list);
            return pageMaster;
        });
        // 异步查询所有的机构Map
        CompletableFuture<Map<Long, String>> mapFuture = CompletableFuture.supplyAsync(() -> sysOrganizationService.selectMap());
        // 异步查询所有的轮次Map
        if (bizType.equals(2)) {
            CompletableFuture<Map<Long, String>> roundFuture = CompletableFuture.supplyAsync(() -> roundService.selectMap());
            roundMap = roundFuture.get();
        }

        PageMaster<Image> pageMaster = listFuture.get();
        List<Image> list = pageMaster.getList();
        Map<Long, String> map = mapFuture.get();
        // response List
        List<ImageListOutVO> respList = new ArrayList<>();

        if (list.size() > 0) {
            // 数据格式化
            for (Image in : list) {
                ImageListOutVO out = new ImageListOutVO();
                BeanUtils.copyProperties(in, out);

                // 提取处理状态文本描述并赋值
                Integer status = in.getStatus();
                out.setFileStatus(Container.IMAGE_STATUS_MAP.get(status));
                // 不可用 可用 解析中

                if (status == 0) {
                    out.setProcessFlagName(Container.IMAGE_PROCESS_MAP.get(in.getProcessFlag()));
                } else {
                    out.setProcessFlagName("");
                }

                // 匹配机构名称
                if (map.containsKey(in.getOrganizationId())) {
                    out.setOrganizationName(map.get(in.getOrganizationId()).toString());
                }

                // 匹配轮次
                if (bizType.equals(2) && roundMap.containsKey(in.getRoundId())) {
                    out.setRoundName(roundMap.get(in.getRoundId()).toString());
                }

                Slide slide = new Slide();
                slide.setImageId(out.getImageId());
                // 禁止删除
                out.setDeleState(0);
                if (slideService.selectImageExist(slide).size() > 0) {
                    out.setDeleState(1);
                }

                respList.add(out);
            }
        }

        PageMaster<ImageListOutVO> resp = new PageMaster<>(respList);
        resp.setTotal(pageMaster.getTotal());
        resp.setPages(pageMaster.getPages());
        resp.setPageNum(pageMaster.getPageNum());
        resp.setPageSize(pageMaster.getPageSize());
        //清除分页缓存
        PageHelper.clearPage();
        return resp;
    }


    /**
     * 项目管理-图像列表
     *
     * @param vo
     * @return
     */
    @Override
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @Transactional(rollbackFor = Exception.class)
    public PageMaster<ImageListOutVO> choiceList(ImageTopicVO vo) throws ExecutionException, InterruptedException {

        Image image = new Image();
        BeanUtils.copyProperties(vo, image);

        // 机构ID
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        image.setOrganizationId(sysUser.getOrganizationId());

        // 业务类型 1 原始切片 2 预测切片
        Integer bizType = image.getBizType();
        // 所有的轮次Map
        Map<Long, String> roundMap = null;

        // 异步查询图像列表
        CompletableFuture<PageMaster<Image>> listFuture = CompletableFuture.supplyAsync(() -> {
            // 分页
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize()).setReasonable(true);
            List<Image> list = null;
            // 添加状态：NULL查全部、0未添加、1已添加
            // 1、查询所有状态
            // 2、未选中：image表为主表 not in slide表中的image_id
            // 3、已选中：slide表为主表 join image获取基础数据
            if (vo.getChoiceState() == null) {
                list = imageMapper.selectListSlfe(image);
            } else if (vo.getChoiceState() == 0) {
                list = imageMapper.selectNotChoicedList(image);
            } else if (vo.getChoiceState() == 1) {
                list = imageMapper.selectChoicedList(image);
            }
            PageMaster pageMaster = new PageMaster<>(list);
            return pageMaster;
        });
        // 异步查询所有的机构Map
        CompletableFuture<Map<Long, String>> mapFuture = CompletableFuture.supplyAsync(() -> sysOrganizationService.selectMap());
        // 异步查询所有的轮次Map
        if (bizType.equals(2)) {
            CompletableFuture<Map<Long, String>> roundFuture = CompletableFuture.supplyAsync(() -> roundService.selectMap());
            roundMap = roundFuture.get();
        }

        PageMaster<Image> pageMaster = listFuture.get();
        List<Image> list = pageMaster.getList();
        Map<Long, String> map = mapFuture.get();
        // response List
        List<ImageListOutVO> respList = new ArrayList<>();

        if (list.size() > 0) {
            // 数据格式化
            for (Image in : list) {
                ImageListOutVO out = new ImageListOutVO();
                BeanUtils.copyProperties(in, out);

                // 提取处理状态文本描述并赋值
                Integer status = in.getStatus();
                out.setFileStatus(Container.IMAGE_STATUS_MAP.get(status));
                // 不可用 可用 解析中

                if (status == 0) {
                    out.setProcessFlagName(Container.IMAGE_PROCESS_MAP.get(in.getProcessFlag()));
                } else {
                    out.setProcessFlagName("");
                }

                // 匹配机构名称
                if (map.containsKey(in.getOrganizationId())) {
                    out.setOrganizationName(map.get(in.getOrganizationId()).toString());
                }

                // 匹配轮次
                if (bizType.equals(2) && roundMap.containsKey(in.getRoundId())) {
                    out.setRoundName(roundMap.get(in.getRoundId()).toString());
                }

                if (vo.getChoiceState() == null) {
                    // 查询选中状态
                    Slide slide = new Slide();
                    slide.setImageId(out.getImageId());
                    slide.setProjectId(vo.getProjectId());

                    if (vo.getReviewRoundId() != null && vo.getReviewRoundId() > 0) {
                        slide.setReviewRoundId(vo.getReviewRoundId());
                    }
                    // 查询当前项目或评审轮次是否选中此图片
                    out.setChoiceState(0);
                    if (slideService.selectImageExist(slide).size() > 0) {
                        out.setChoiceState(1);
                    }
                } else if (vo.getChoiceState() == 0) {
                    out.setChoiceState(0);
                } else if (vo.getChoiceState() == 1) {
                    out.setChoiceState(1);
                }

                respList.add(out);
            }
        }

        PageMaster<ImageListOutVO> resp = new PageMaster<>(respList);
        resp.setTotal(pageMaster.getTotal());
        resp.setPages(pageMaster.getPages());
        resp.setPageNum(pageMaster.getPageNum());
        resp.setPageSize(pageMaster.getPageSize());
        //清除分页缓存
        PageHelper.clearPage();
        return resp;
    }


    /**
     * 查询图像列表 - 通过 projectId 查询
     *
     * @param projectId
     * @return
     */
    public List<ImageListVO> selectImageListByPorjectId(Long projectId) {
        return imageMapper.selectImageListByPorjectId(projectId);
    }

    /**
     * 查询单个图像信息
     *
     * @param image
     * @return
     */
    @Override
    public Image selectById(Long image) {
        return imageMapper.selectById(image);
    }

    /**
     * 通过图片ID查询切片
     *
     * @param imageId
     * @return
     */
    @Override
    public Integer selectSlideCountByImageId(Long imageId) {
        return imageMapper.selectSlideCountByImageId(imageId);
    }


    /**
     * 标注组选片入口预览图像列表
     *
     * @param image
     * @return
     */
    @Override
    public List<Image> selectImageChooseList(Image image) {
        return imageMapper.selectImageChooseList(image);
    }

    /**
     * 通过ID批量修改图片状态
     *
     * @param imageIdList
     * @return
     */
    @Override
    public int updateBatchIds(ImageTopicBatchIdsVO imageIdList) {
        return imageMapper.updateBatchIds(imageIdList);
    }

    /**
     * 标注组图像列表
     *
     * @param image
     * @return
     */
    @Override
    public List<Image> selectImageAnnotationList(Image image) {
        return imageMapper.selectImageAnnotationList(image);
    }

    /**
     * 运维图像删除
     *
     * @param imageId
     * @return
     */
    @Override
    public int deleteById(Long imageId) {
        return imageMapper.deleteById(imageId);
    }

    /**
     * 逻辑删除单张切片
     *
     * @param imageId
     * @return
     */
    @Override
    public int updateDeleteFlagById(Long imageId) {
        // If the image using,forbid delete
        if (specialImageMapper.selectListByImageId(imageId).size() > 0) {
            return 0;
        }
        // not using , delete
        return imageMapper.updateDeleteFlagById(imageId);
    }

    /***
     * 批量删除图片（逻辑删除）
     * @param ids
     * @return
     */
    @Override
    public List<Long> updateDeleteFlagBatchIds(ImageBatchIdsVO ids) {
        // return imageMapper.updateDeleteFlagBatchIds(ids);
        // Check: is using
        List<Long> usingIds = specialImageMapper.selectImageIdsListByImageIds(ids);

        List<Long> forbidIds = new ArrayList<>();
        // remove id in forbid list
        for (Long imageId : ids.getImageIdList()) {
            if (usingIds.contains(imageId)) {
                forbidIds.add(imageId);
                continue;
            }

            if (imageId > 0) {
                Slide slide = new Slide();
                slide.setImageId(imageId);
                // 查切片表中有没有绑定此图片
                if (slideService.selectImageExist(slide).size() > 0) {
                    imageMapper.updateDeleteFlagById(imageId);
                }
            }
        }
        return forbidIds;
    }

    /**
     * 更改图像上传状态
     *
     * @param imageIdList 图像ID列表
     * @return
     */
    public void updateProcessFlagByIdList(List imageIdList) {
        imageMapper.updateProcessFlagByIdList(imageIdList);
    }

    /**
     * 关联切片与专题、组织ID，没有专题则新添加
     *
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateById(ImageUpdateVO vo) throws Exception {
        Image image = new Image();
        BeanUtils.copyProperties(vo, image);

        // 获取当前登录用户Id
        Long loginUser = SecurityUtils.getUserId();
        image.setUpdateBy(loginUser);
        return imageMapper.updateById(image);
    }
}
