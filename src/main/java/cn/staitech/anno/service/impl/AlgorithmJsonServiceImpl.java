package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AlgorithmAssessment;
import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.mapper.AlgorithmAssessmentMapper;
import cn.staitech.anno.mapper.AlgorithmJsonMapper;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gjt
 * @since 2023-10-17
 */
@Service
public class AlgorithmJsonServiceImpl extends ServiceImpl<AlgorithmJsonMapper, AlgorithmJson> implements AlgorithmJsonService {

    @Resource
    private AlgorithmJsonMapper algorithmJsonMapper;

    @Resource
    private AlgorithmAssessmentMapper algorithmAssessmentMapper;

    @Override
    public void examineComparison(Long algorithmJsonId) throws Exception {
        // 查询详情信息
        AlgorithmJson algorithmJson = algorithmJsonMapper.selectById(algorithmJsonId);

        if(algorithmJson == null){
            throw new Exception("未发现文件信息");
        }
        // 查询考题详情
        AlgorithmAssessment algorithmAssessment = algorithmAssessmentMapper.selectById(algorithmJson.getAlgorithmAssessmentId());
        if(algorithmAssessment == null){
            throw new Exception("未发现考题信息");
        }

        // 构建map
        JSONObject markingJsonObject = new JSONObject();
        markingJsonObject.put("annotation_json_url",algorithmAssessment.getAnnotationJsonUrl());
        markingJsonObject.put("algorithm_json_url",algorithmJson.getAlgorithmJsonUrl());
        markingJsonObject.put("slide_id",algorithmJson.getSlideId());
        markingJsonObject.put("json_name",algorithmJson.getAlgorithmJsonName());
        markingJsonObject.put("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        // 调用python接口

    }




}
