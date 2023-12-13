package cn.staitech.anno.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.staitech.anno.enums.SpecialEnum;
import cn.staitech.anno.mapper.ProjectMapper;
import cn.staitech.anno.mapper.SpecialMapper;
import cn.staitech.anno.mapper.SpecialReclaimMapper;
import cn.staitech.anno.mapper.SubImageMapper;
import cn.staitech.anno.service.SpecialService;
import cn.staitech.anno.utils.TimeUtils;
import cn.staitech.anno.vo.special.*;
import cn.staitech.common.core.exception.ServiceException;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.staitech.anno.enums.SpecialEnum.DEL_FLAG_1;

/**
 * @author gjt.
 * @data 2023/5/29 9:14
 */
@Service
@Slf4j
public class SpecialServiceImpl extends ServiceImpl<SpecialMapper, Special> implements SpecialService {
    @Resource
    private SpecialMapper specialMapper;

    @Resource
    private SpecialReclaimMapper specialReclaimMapper;

    @Resource
    private SubImageMapper subImageMapper;

    @Resource
    private ProjectMapper projectMapper;

    /**
     * 查询专题阅片下的专题列表
     *
     * @return Special
     */
    @Override
    public List<SpecialResVo> selectSpecialReadFilm(SpecialSelectVo req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);
        Special special = new Special();
        org.springframework.beans.BeanUtils.copyProperties(req, special);
        special.setUserName(req.getCreateBy());
        special.setDelFlag(SpecialEnum.DEL_FLAG_0.value());
        // 判断当前用户是否为admin
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())) {
            special.setCreateBy(SecurityUtils.getUserId());
            special.setSruStatus(0L);
        }
        List<SpecialResVo> specialResVos = specialMapper.selectSpecialReadFilm(special);
        // 根据专题id查询项目数量
        for (SpecialResVo specialResVo : specialResVos) {
            int projectNum = projectMapper.selectProjectCount(specialResVo.getSpecialId());
            specialResVo.setProjectNum((long) projectNum);
        }
        return specialResVos;
    }

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return Special
     */
    @Override
    public SpecialResVo selectSpecialId(Long specialId) {
        if (!Optional.ofNullable(specialId).isPresent()) {
            throw new ServiceException("专题不可为空");
        }
        return specialMapper.selectSpecialId(specialId);
    }

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return
     */
    @Override
    public Special selectSpecialById(Long specialId) {
        return specialMapper.selectSpecialById(specialId);
    }

    /**
     * 查询专题信息
     *
     * @param special 专题信息
     * @return List<Special>
     */
    @Override
    public List<SpecialResVo> selectList(Special special) {
        List<SpecialResVo> specialResVos = specialMapper.selectList(special);
        // 根据专题id查询项目数量
        for (SpecialResVo specialResVo : specialResVos) {
            int projectNum = projectMapper.selectProjectCount(specialResVo.getSpecialId());
            specialResVo.setProjectNum((long) projectNum);
            int slideNum = specialMapper.selectSpecialSlideCount(specialResVo.getSpecialId());
            specialResVo.setSlideNum((long) slideNum);
        }
        return specialResVos;
    }

    /**
     * 专题统计
     *
     * @param special 专题信息
     * @return List<Special>
     */
    @Override
    public List<SpecialStatisticsListVO> specialStatistics(SpecialStatisticsQueryVO special) {
        List<SpecialStatisticsListVO> specialStatistics;
        if (SpecialStatisticsQueryVO.isAdmin(special.getUserId())) {
            specialStatistics = specialMapper.specialStatisticsAdmin(special);
            specialStatistics.forEach(s -> {
                // 查询专题下的项目ID
                List<Long> projectId = specialMapper.queryProjectIdAdminBySpecialId(s.getSpecialId());
                getCompletionRate(s, projectId);
            });
        } else {
            // 非admin用户查询专题列表(包含项目总数)
            specialStatistics = specialMapper.specialStatistics(special);
            specialStatistics.forEach(s -> {
                SpecialStatisticsListVO build = SpecialStatisticsListVO.builder().userId(s.getUserId()).specialId(s.getSpecialId()).build();
                // 查询专题下的项目ID
                List<Long> projectId = specialMapper.queryProjectIdBySpecialId(build);
                getCompletionRate(s, projectId);
            });
        }
        return specialStatistics;
    }

    /**
     * 获取完成率
     *
     * @param s
     * @param projectId
     */
    private void getCompletionRate(SpecialStatisticsListVO s, List<Long> projectId) {
//        AtomicInteger projectIncomplete = new AtomicInteger();
        if (ObjectUtils.isEmpty(projectId)) {
            s.setProjectComplete(0);
            s.setProjectIncomplete(0);
            s.setSpecialCompletionRate("0" + "%");
        } else {
            // 统计未完成项目数
            int incomplete = 0;
            for (Long p : projectId) {
                // 查询项目下所有切片的人工诊断状态
               /* List<Long> longs = specialMapper.queryDiagnosisByProjectId(p);
                if (ObjectUtils.isEmpty(longs)) {
                    projectIncomplete.incrementAndGet();
                } else {
                    projectIncomplete.addAndGet(longs.contains(0) ? 1 : 0);
                }*/
                //通过项目id查询所有切片（备注：已完成项目的定义是：当前项目下的所有切片均进行了人工诊断）
                List<SpecialSlideStatisticsVO> ssvoList = specialMapper.queryDiagnosisByProjectId(p);
                if (CollectionUtils.isNotEmpty(ssvoList)) {
                    for (SpecialSlideStatisticsVO vo : ssvoList) {
                        //人工诊断状态：0未诊断，1已诊断
                        int diagnosis = vo.getDiagnosis();
                        int totalCount = vo.getTotalCount();
                        if (diagnosis == 0) {
                            if (totalCount > 0) {
                                incomplete++;
                                log.info("专题名称1:" + s.getSpecialName() + " 未完成数量是：" + incomplete);
                            }
                        }
                    }
                } else {
                    incomplete++;
                    log.info("专题名称2:" + s.getSpecialName() + " 未完成数量是：" + incomplete);
                }
            }
            log.info("专题名称3:" + s.getSpecialName() + " 未完成数量是：" + incomplete);
            Integer total = s.getProjectTotal();
//            int incomplete = projectIncomplete.intValue();
            int complete = total - incomplete;

            s.setProjectComplete(complete);
            s.setProjectIncomplete(incomplete);

            // 完成率
            int completionRate = NumberUtil.div(String.valueOf(complete), String.valueOf(total), 2).multiply(BigDecimal.valueOf(100)).intValue();
            s.setSpecialCompletionRate(completionRate + "%");
        }
    }

    /**
     * 更新专题信息
     *
     * @param req 专题信息
     * @return true||false
     */
    @Override
    public int update(SpecialUpdateVo req) {
        Special special = new Special();
        special.setSpecialNumber(req.getSpecialNumber());
        special.setSpecialId(req.getSpecialId());
        // 查询编号是否存在
        Special specialNumber = specialMapper.selectSpecialNumber(special);
        if (specialNumber != null) {
            throw new ServiceException("当前专题编号已存在,禁止重复添加");
        }
        Special specials = new Special();
        specials.setSpecialName(req.getSpecialName());
        specials.setSpecialId(req.getSpecialId());
        Special specialName = specialMapper.selectSpecialNumber(specials);
        if (specialName != null) {
            throw new ServiceException("当前专题名称已存在,禁止重复添加");
        }
        // 判断当前指标中的标签是否标注使用
        if (req.getIndicatorId() != null) {
            // 查询专题详情
            Special specialBy = specialMapper.selectSpecialById(req.getSpecialId());
            if (!Objects.equals(specialBy.getIndicatorId(), req.getIndicatorId())) {
                List<Special> specialList = specialMapper.selectSpecialCategoryList(req.getSpecialId());
                if (specialList.size() > 0) {
                    throw new ServiceException("当前病理指标使用中,禁止取消关联");
                }
            }
        }
        BeanUtils.copyProperties(req, special);
        special.setUpdateBy(SecurityUtils.getUserId());
        specialMapper.update(special);
        return specialMapper.update(special);
    }

    /**
     * 更新专题交付状态
     */
    @Override
    public int updateDeliveryStatus(Special special) {
        return specialMapper.update(special);
    }

    /**
     * 删除专题表中信息
     *
     * @param req 专题信息
     * @return true||false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDelFlag(SpecialDeleteVo req) {
        Special special = new Special();
        special.setSpecialId(req.getSpecialId());
        special.setDelFlag(req.getDelFlag());
        special.setUpdateBy(SecurityUtils.getUserId());
        int result = specialMapper.updateDelFlag(special);
        // 如果更新状态成功且状态为1
        if (result > 0 && special.getDelFlag().equals(DEL_FLAG_1.value())) {
            // 添加专题回收表中
            SpecialReclaim specialReclaim = new SpecialReclaim();
            specialReclaim.setReclaimBy(SecurityUtils.getUserId());
            specialReclaim.setSpecialId(special.getSpecialId());
            specialReclaim.setExpireTime(TimeUtils.dateIncreases());
            // 查询项目数量
            int projectNum = projectMapper.selectProjectCount(req.getSpecialId());
            specialReclaim.setProjectNum((long) projectNum);
            // 查询切片数量
            int slideNum = subImageMapper.selectSpecialCounts(req.getSpecialId());
            specialReclaim.setSlideNum((long) slideNum);
            specialReclaimMapper.insert(specialReclaim);
        }
        return result;
    }

    /**
     * 更新专题状态
     *
     * @param req 专题信息
     * @return true||false
     */
    @Override
    public int updateStatus(SpecialStatusVo req) {
        Special special = new Special();
        special.setSpecialId(req.getSpecialId());
        special.setStatus(req.getStatus());
        special.setUpdateBy(SecurityUtils.getUserId());
        return specialMapper.updateStatus(special);
    }

}
