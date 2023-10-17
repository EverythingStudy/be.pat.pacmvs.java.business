package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AlgorithmAssessment;
import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.Slide;
import cn.staitech.anno.domain.assessment.in.CreateAssessmentDataIn;
import cn.staitech.anno.domain.assessment.in.CreateAssessmentIn;
import cn.staitech.anno.domain.assessment.in.GetAssessmentListIn;
import cn.staitech.anno.domain.assessment.out.GetAssessmentListOut;
import cn.staitech.anno.mapper.AlgorithmAssessmentMapper;
import cn.staitech.anno.mapper.AlgorithmJsonMapper;
import cn.staitech.anno.mapper.SlideMapper;
import cn.staitech.anno.service.AlgorithmAssessmentService;
import cn.staitech.anno.service.MarkingService;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.utils.date.DateUtils;
import cn.staitech.common.core.domain.PageResponse;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.similarities.Lambda;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
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
 * @author gjt
 * @since 2023-10-17
 */
@Service
@Slf4j
public class AlgorithmAssessmentServiceImpl extends ServiceImpl<AlgorithmAssessmentMapper, AlgorithmAssessment> implements AlgorithmAssessmentService {

    @Autowired
    private MarkingService markingService;

    @Resource
    private AlgorithmJsonMapper algorithmJsonMapper;

    @Resource
    private SlideMapper slideMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createAssessment(CreateAssessmentIn req) {
        log.info("生成算法考核接口开始：");

        List<CreateAssessmentDataIn> slideList = req.getSlideList();
        List<AlgorithmAssessment> algorithmAssessments = slideList.stream().map(e -> {
            AlgorithmAssessment resp = new AlgorithmAssessment();
            BeanUtils.copyProperties(e, resp);
            resp.setCreateBy(SecurityUtils.getUserId());
            resp.setCreateTime(new Date());
            // 插入json文件返回数据
            String urlPath;
            try {
                urlPath = markingService.slideJsonExport(e.getSlideId());
            } catch (Exception exception) {
                log.error(exception.toString());
                throw new RuntimeException(MessageSource.M("ERROR_GENERATE_JSON"));
            }
            String s = StringUtils.substringAfterLast(urlPath, File.separator);
            resp.setAnnotationJsonName(s);
            resp.setAnnotationJsonUrl(urlPath);
            return resp;

        }).collect(Collectors.toList());

        saveBatch(algorithmAssessments);
        //修改切片是否生成状态
        return R.ok();
    }

    @Override
    public PageResponse<GetAssessmentListOut> getAssessmentList(GetAssessmentListIn req) {
        log.info("算法考核列表分页查询接口开始：");

        PageResponse resp = new PageResponse();

        LambdaQueryWrapper<AlgorithmAssessment> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(req.getImageName()), AlgorithmAssessment::getImageName, req.getImageName());
        qw.eq(AlgorithmAssessment::getProjectId, req.getProjectId());
        qw.eq(!ObjectUtils.isEmpty(req.getCategoryId()), AlgorithmAssessment::getCategoryId, req.getCategoryId());
        if (!CollectionUtils.isEmpty(req.getCreateTimeParams())) {
            try {
                Date date = DateUtils.addAndSubtractDaysByCalendar(DateUtils.stringToDate((String) req.getCreateTimeParams().get("endTime"), "yyyy-MM-dd"), 1);
                qw.lt(AlgorithmAssessment::getCreateTime, date);
                qw.ge(AlgorithmAssessment::getCreateTime, DateUtils.stringToDate((String) req.getCreateTimeParams().get("beginTime"), "yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("时间入参格式转化异常-{}", e);
                throw new RuntimeException("时间入参格式转化异常!");
            }

        }

        Page<AlgorithmAssessment> page = PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AlgorithmAssessment> algorithmAssessments = this.baseMapper.selectList(qw);
        List<GetAssessmentListOut> collect = new ArrayList<>();

        if (!CollectionUtils.isEmpty(algorithmAssessments)) {
            LambdaQueryWrapper<AlgorithmJson> qw2 = new LambdaQueryWrapper<>();
            collect = algorithmAssessments.stream().map(e -> {
                GetAssessmentListOut resp2 = new GetAssessmentListOut();
                BeanUtils.copyProperties(e, resp);
                qw2.eq(AlgorithmJson::getAlgorithmAssessmentId, e.getAlgorithmAssessmentId());
                List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(qw2);
                if (!CollectionUtils.isEmpty(algorithmJsons)) {
                    resp2.setAlgorithmJsonNames(algorithmJsons.stream().map(AlgorithmJson::getAlgorithmJsonName).collect(Collectors.toList()));
                }

                return resp2;
            }).collect(Collectors.toList());
        }


        resp.setTotal(page.getTotal());
        resp.setList(collect);
        resp.setPages(page.getPages());


        return resp;
    }
}
