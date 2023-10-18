package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.AlgorithmAssessment;
import cn.staitech.anno.domain.AlgorithmJson;
import cn.staitech.anno.domain.algorithmJson.in.SelectGeoJson;
import cn.staitech.anno.domain.algorithmJson.out.SelectGeoJsonList;
import cn.staitech.anno.domain.marking.SlideRes;
import cn.staitech.anno.mapper.AlgorithmAssessmentMapper;
import cn.staitech.anno.mapper.AlgorithmJsonMapper;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.RemoteLabelService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private RemoteLabelService remoteLabelService;

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
        com.alibaba.fastjson2.JSONObject markingJsonObject = new com.alibaba.fastjson2.JSONObject();
        markingJsonObject.put("annotation_json_url",algorithmAssessment.getAnnotationJsonUrl());
        markingJsonObject.put("algorithm_json_url",algorithmJson.getAlgorithmJsonUrl());
        markingJsonObject.put("slide_id",algorithmJson.getSlideId());
        markingJsonObject.put("json_name",algorithmJson.getAlgorithmJsonName());
        markingJsonObject.put("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        // 调用python接口
        remoteLabelService.algoExamine(markingJsonObject);
    }

    @Override
    public SelectGeoJsonList getGeoJson(SelectGeoJson selectGeoJson) throws Exception {
        // 获取json列表，判断
        List<Long> algorithmJsonList = selectGeoJson.getAlgorithmJsonList();
        if(algorithmJsonList.size() == 1){
            AlgorithmJson algorithmJson = algorithmJsonMapper.selectById(algorithmJsonList.get(0));
            if(algorithmJson != null){
                if(Objects.equals(algorithmJson.getJsonType(), "0")){
                    throw new Exception("禁止只选择一个人工标注的JSON");
                }
            }
        }
        QueryWrapper<AlgorithmJson> algorithmJsonQueryWrapper = new QueryWrapper<>();
        algorithmJsonQueryWrapper.in("algorithm_json_id",algorithmJsonList);
        // 查询选中的json列表
        List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(algorithmJsonQueryWrapper);
        // 循环json
        JSONArray features = new JSONArray();
        JSONArray labelInfo = new JSONArray();
        for(AlgorithmJson algorithmJson:algorithmJsons){
            // 获取json文件路径
            if(algorithmJson.getAlgorithmJsonUrl() != null){
                JSONObject jsonObject = getGeoJson(algorithmJson.getAlgorithmJsonUrl());
                JSONArray featuresJson = jsonObject.getJSONArray("features");
                features.addAll(featuresJson);
                JSONArray labelNameJson = jsonObject.getJSONArray("label_name");
                labelInfo.addAll(labelNameJson);
            }
        }
        SelectGeoJsonList selectGeoJsonList = new SelectGeoJsonList();
        selectGeoJsonList.setFeatures(features);
        selectGeoJsonList.setLabel_info(labelInfo);
        return selectGeoJsonList;


    }



    public static JSONObject getGeoJson(String jsonUrl) {
        JSONObject parse = new com.alibaba.fastjson.JSONObject();
        File jsonFile = new File(jsonUrl);
        if (jsonFile.exists()) {
            //通过getStr方法获取json文件的内容
            String jsonData = getStr(jsonFile);
            //转json对象
            parse = (JSONObject) JSONObject.parse(jsonData);
            return parse;
        }
        return parse;
    }

    public static String getStr(File jsonFile) {
        String jsonStr;
        try {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




}
