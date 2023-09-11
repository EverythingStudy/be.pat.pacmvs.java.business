package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.ImageConstant;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.Topic;
import cn.staitech.anno.domain.image.in.ImageBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageListVO;
import cn.staitech.anno.domain.image.in.ImageTopicBatchIdsVO;
import cn.staitech.anno.domain.image.in.ImageUpdateVO;
import cn.staitech.anno.domain.image.out.ImageListOutVO;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.SpecialImageMapper;
import cn.staitech.anno.mapper.TopicMapper;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.RoundService;
import cn.staitech.anno.service.SysOrganizationService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.utils.date.DateUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    private TopicMapper topicMapper;

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

        // 业务类型 1 原始切片 2 预测切片
        Integer bussinessType = image.getBusinessType();
        // 所有的轮次Map
        Map<Long, String> roundMap = null;

        // 异步查询图像列表
        CompletableFuture<PageMaster<Image>> listFuture = CompletableFuture.supplyAsync(() -> {
            // 分页
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize()).setReasonable(true);
            log.info("分页参数：{} {}", vo.getPageNum(), vo.getPageSize());
            List<Image> list = imageMapper.selectList(image);
            PageMaster pageMaster = new PageMaster<>(list);
            return pageMaster;
        });
        // 异步查询所有的机构Map
        CompletableFuture<Map<Long, String>> mapFuture = CompletableFuture.supplyAsync(() -> sysOrganizationService.selectMap());
        // 异步查询所有的轮次Map
        if (bussinessType.equals(2)) {
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
                out.setFileStatus(ImageConstant.IMAGE_STATUS_MAP.get(status));

                if (status == 0) {
                    out.setProcessFlagName(ImageConstant.IMAGE_PROCESS_MAP.get(in.getProcessFlag()));
                } else {
                    out.setProcessFlagName("");
                }

                // 匹配机构名称
                out.setOrganizationName(map.get(in.getOrganizationId()).toString());

                // 匹配轮次
                if (bussinessType.equals(2)) {
                    out.setRoundName(roundMap.get(in.getRoundId()).toString());
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
        for (Long id : ids.getImageIdList()) {
            if (usingIds.contains(id)) {
                forbidIds.add(id);
                continue;
            }
            imageMapper.updateDeleteFlagById(id);
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

        Long uid = SecurityUtils.getLoginUser().getUserid();
        log.info("uid -------------- {} ", uid);
        image.setUpdateBy(uid);

        String time = DateUtils.getCurrentHHmmssString("yyyy-MM-dd HH:mm:ss");

        // 处理Topic逻辑，有则修改，无则添加
        if (image.getTopicId().equals(999999999L)) {
            // 如果存在逻辑删除的Topic：insert会报错，insert前查询，如果有修改defFlag = 1
            QueryWrapper<Topic> qWrapper = new QueryWrapper<>();
            qWrapper.eq("topic_name", vo.getTopicName());

            Topic topic = topicMapper.selectOne(qWrapper);
            if (topic != null) {
                topic.setUpdateBy(uid);
                topic.setUpdateTime(time);
                topic.setDelFlag(1);

                topicMapper.updateById(topic);
            } else {
                // construct a new Topic object
                topic = Topic.builder()
                        .topicName(vo.getTopicName())
                        .createBy(uid)
                        .updateBy(uid)
                        .createTime(time)
                        .updateTime(time)
                        .delFlag(1)
                        .build();

                // insert Topic object
                topicMapper.insert(topic);
            }

            image.setTopicId(topic.getTopicId());
            image.setTopicName(topic.getTopicName());
        }
        return imageMapper.updateById(image);
    }
}
