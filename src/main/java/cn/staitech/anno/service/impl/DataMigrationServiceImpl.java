package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ExamineScore;
import cn.staitech.anno.domain.Image;
import cn.staitech.anno.domain.QuestionBank;
import cn.staitech.anno.domain.RecentlyVisited;
import cn.staitech.anno.mapper.AlgorithmAssessmentMapper;
import cn.staitech.anno.mapper.AlgorithmJsonMapper;
import cn.staitech.anno.mapper.ExamineScoreMapper;
import cn.staitech.anno.mapper.FilesMapper;
import cn.staitech.anno.mapper.ImageMapper;
import cn.staitech.anno.mapper.ProjectMapper;
import cn.staitech.anno.mapper.QuestionBankMapper;
import cn.staitech.anno.mapper.RecentlyVisitedMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.mapper.SysUserMapper;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.project.mapper.MarkingMapperV1;
import cn.staitech.anno.project.service.MarkingServiceV1;
import cn.staitech.anno.service.AlgorithmAssessmentService;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.anno.service.DataMigrationService;
import cn.staitech.anno.service.ExamineScoreService;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.IQuestionBankService;
import cn.staitech.anno.service.ImageService;
import cn.staitech.anno.service.RecentlyVisitedService;
import cn.staitech.anno.utils.StatisticListUtils;
import cn.staitech.anno.vo.algorithm.AlgorithmAssessment;
import cn.staitech.anno.vo.algorithm.AlgorithmJson;
import cn.staitech.anno.vo.files.Files;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author wudi
 * @Date 2023/11/8 19:19
 * @desc
 */
@Service
@Slf4j
public class DataMigrationServiceImpl implements DataMigrationService {
    @Resource
    private ImageMapper imageMapper;
    @Autowired
    private ImageService imageService;

    @Resource
    private AlgorithmAssessmentMapper algorithmAssessmentMapper;
    @Autowired
    private AlgorithmAssessmentService algorithmAssessmentService;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AlgorithmJsonMapper algorithmJsonMapper;
    @Autowired
    private AlgorithmJsonService algorithmJsonService;

    @Resource
    private SlideMapper slideMapper;

    @Resource
    private ExamineScoreMapper examineScoreMapper;
    @Autowired
    private ExamineScoreService examineScoreService;

    @Resource
    private FilesMapper filesMapper;
    @Autowired
    private FilesService filesService;


    @Resource
    private MarkingMapperV1 markingMapperV1;
    @Autowired
    private MarkingServiceV1 markingServiceV1;

    @Resource
    private QuestionBankMapper questionBankMapper;
    @Resource
    private IQuestionBankService iquestionBankService;

    @Resource
    private RecentlyVisitedMapper recentlyVisitedMapper;
    @Autowired
    private RecentlyVisitedService recentlyVisitedService;


    @Resource
    SysUserMapper sysUserMapper;


    @Override
    public int imageData() {
        log.info("image表数据迁移开始：");
        LambdaQueryWrapper<Image> qw = new LambdaQueryWrapper<>();
        List<Image> images = imageMapper.selectList(qw);
        images.forEach(e -> {
            if (StringUtils.isNotEmpty(e.getImagePath())) {
                e.setImagePath(e.getImagePath().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(e.getOrganizationId())));

            }
            if (StringUtils.isNotEmpty(e.getImagePath())) {
                e.setImageUrl(e.getImageUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(e.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getImagePath())) {
                e.setCacheUrl(e.getCacheUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(e.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getImagePath())) {
                e.setThumbUrl(e.getThumbUrl().replace("/file/statics", "/file/statics/" + StatisticListUtils.getFourNumber(e.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getImagePath())) {
                e.setMacroUrl(e.getMacroUrl().replace("/file/statics", "/file/statics/" + StatisticListUtils.getFourNumber(e.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getImagePath())) {
                e.setLabelUrl(e.getLabelUrl().replace("/file/statics", "/file/statics/" + StatisticListUtils.getFourNumber(e.getOrganizationId())));
            }

        });
        boolean b = imageService.updateBatchById(images);
        log.info("image表数据条数：" + images.size());
        return images.size();
    }

    /**
     * 算法考核表tb_algorithm_assessment
     *
     * @return
     */
    @Override
    public int assessmentData() {
        log.info("assessment表数据迁移开始：");
        LambdaQueryWrapper<AlgorithmAssessment> wq = new LambdaQueryWrapper<>();
        List<AlgorithmAssessment> algorithmAssessments = algorithmAssessmentMapper.selectList(wq);
        algorithmAssessments.forEach(e -> {
            SysUser sysUser = sysUserMapper.selectUserById(e.getCreateBy());
            if (StringUtils.isNotEmpty(e.getThumbUrl())) {
                e.setThumbUrl(e.getThumbUrl().replace("/file/statics", "/file/statics/" + StatisticListUtils.getFourNumber(sysUser.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getAnnotationJsonUrl())) {
                e.setAnnotationJsonUrl(e.getAnnotationJsonUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
            }
        });
        boolean b = algorithmAssessmentService.updateBatchById(algorithmAssessments);
        log.info("assessment表数据迁移{}数量：{}", b, algorithmAssessments.size());
        return algorithmAssessments.size();
    }

    /**
     * 算法考核表tb_algorithm_json
     *
     * @return
     */
    @Override
    public int algorithmJsonData() {
        log.info("tb_algorithm_json表数据迁移开始：");
        LambdaQueryWrapper<AlgorithmJson> wq = new LambdaQueryWrapper<>();
        List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(wq);
        algorithmJsons.forEach(e -> {
            SysUser sysUser = sysUserMapper.selectUserById(e.getCreateBy());
            if (StringUtils.isNotEmpty(e.getAlgorithmJsonUrl())) {
                e.setAlgorithmJsonUrl(e.getAlgorithmJsonUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
            }
        });
        boolean b = algorithmJsonService.updateBatchById(algorithmJsons);
        log.info("tb_algorithm_json表数据迁移{}数量：{}", b, algorithmJsons.size());
        return algorithmJsons.size();
    }

    /**
     * 考核结果表tb_examine_score
     *
     * @return
     */
    @Override
    public int examineScoreData() {
        log.info("tb_examine_score表数据迁移开始：");
        LambdaQueryWrapper<ExamineScore> wq = new LambdaQueryWrapper<>();
        List<ExamineScore> examineScores = examineScoreMapper.selectList(wq);
        examineScores.forEach(e -> {
            SysUser sysUser = sysUserMapper.selectUserById(e.getCreateBy());
            if (StringUtils.isNotEmpty(e.getGeojsonUrl())) {
                e.setGeojsonUrl(e.getGeojsonUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getExaminationGeojsonUrl())) {
                e.setExaminationGeojsonUrl(e.getExaminationGeojsonUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
            }

        });
        boolean b = examineScoreService.updateBatchById(examineScores);
        log.info("tb_examine_score表数据迁移{}数量：{}", b, examineScores.size());
        return examineScores.size();
    }

    /**
     * 考核结果表tb_files
     *
     * @return
     */
    @Override
    public int filesData() {
        log.info("tb_files表数据迁移开始：");
        LambdaQueryWrapper<Files> wq = new LambdaQueryWrapper<>();
        List<Files> files = filesMapper.selectList(wq);
        files.forEach(e -> {
            SysUser sysUser = sysUserMapper.selectUserById(e.getCreateBy());
            if (StringUtils.isNotEmpty(e.getFilesPath())) {
                e.setFilesPath(e.getFilesPath().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
            }
            if (StringUtils.isNotEmpty(e.getFilesUrl())) {
                e.setFilesUrl(e.getFilesUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
            }

        });
        boolean b = filesService.updateBatchById(files);
        log.info("tb_files表数据迁移{}数量：{}", b, files.size());
        return files.size();
    }

    /**
     * 考核结果表tb_marking
     *
     * @return
     */
    @Override
    public int markingData() {
        log.info("tb_marking表数据迁移开始：");
        boolean flag = true;
        int i = 1;
        int sum = 0;
        while (flag) {
            Page<Marking> page = PageHelper.startPage(i, 1000);
            LambdaQueryWrapper<Marking> qw = new LambdaQueryWrapper<>();
            qw.select(Marking::getMarkingId, Marking::getImageUrl, Marking::getCreateBy);
            List<Marking> markings = markingMapperV1.selectList(qw);
            if (markings.size() > 0) {
                markings.forEach(e -> {
                    SysUser sysUser = sysUserMapper.selectUserById(e.getCreateBy());
                    if (StringUtils.isNotEmpty(e.getImageUrl())) {
                        e.setImageUrl(e.getImageUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(sysUser.getOrganizationId())));
                    }
                });
                markingServiceV1.updateBatchById(markings);
                sum += markings.size();
                i++;
            } else {
                break;
            }

        }
        log.info("tb_marking表数据迁移数据量：" + sum);
        return sum;
    }

    /**
     * 标注考核tb_question_bank
     *
     * @return
     */
    @Override
    public int questionBankData() {
        log.info("tb_question_bank表数据迁移开始：");
        LambdaQueryWrapper<QuestionBank> wq = new LambdaQueryWrapper<>();
        List<QuestionBank> questionBanks = questionBankMapper.selectList(wq);
        questionBanks.forEach(e -> {
            if (StringUtils.isNotEmpty(e.getGeojsonUrl())) {
                e.setGeojsonUrl(e.getGeojsonUrl().replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(e.getOrganizationId())));
            }
        });
        boolean b = iquestionBankService.updateBatchById(questionBanks);
        log.info("tb_question_bank表数据迁移{}数量：{}", b, questionBanks.size());
        return questionBanks.size();
    }

    /**
     * 项目记录tb_recently_visited
     *
     * @return
     */
    @Override
    public int recentlyVisitedData() {
        log.info("tb_recently_visited表数据迁移开始：");
        LambdaQueryWrapper<RecentlyVisited> wq = new LambdaQueryWrapper<>();
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedMapper.selectList(wq);
        recentlyVisiteds.forEach(e -> {
            SysUser sysUser = sysUserMapper.selectUserById(e.getUserId());
            if (StringUtils.isNotEmpty(e.getThumbUrl())) {
                e.setThumbUrl(e.getThumbUrl().replace("/file/statics", "/file/statics/" + StatisticListUtils.getFourNumber(sysUser.getOrganizationId())));
            }
        });
        boolean b = recentlyVisitedService.updateBatchById(recentlyVisiteds);
        log.info("tb_recently_visited表数据迁移{}数量：{}", b, recentlyVisiteds.size());
        return recentlyVisiteds.size();
    }


    public static void main(String[] args) {
        String s = "/home/pat_saas/Slides/TEST123456/20230725153323334_1978.tif";
        String replace = s.replace("/home/pat_saas", "/home/pat_saas/" + StatisticListUtils.getFourNumberNoSlide(25L));
        System.out.println(replace);
        String s2 = "/file/statics/thumbnail/20231108/259/0.jpg";
        String re = s2.replace("/file/statics", "/file/statics/" + StatisticListUtils.getFourNumber(26L));
        System.out.println(re);
    }
}
