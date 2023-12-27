package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.PathologicalIndicatorCategory;
import cn.staitech.anno.domain.Structure;
import cn.staitech.anno.mapper.AlgorithmAssessmentMapper;
import cn.staitech.anno.mapper.AlgorithmJsonMapper;
import cn.staitech.anno.mapper.PathologicalIndicatorCategoryMapper;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.service.AlgorithmJsonService;
import cn.staitech.anno.service.StructureService;
import cn.staitech.anno.utils.GeometryUtil;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.algorithm.AlgorithmAssessment;
import cn.staitech.anno.vo.algorithm.AlgorithmJson;
import cn.staitech.anno.vo.algorithm.in.SelectGeoJson;
import cn.staitech.anno.vo.algorithm.out.SelectGeoJsonList;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.RemoteLabelService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class AlgorithmJsonServiceImpl extends ServiceImpl<AlgorithmJsonMapper, AlgorithmJson> implements AlgorithmJsonService {

    @Resource
    private AlgorithmJsonMapper algorithmJsonMapper;

    @Resource
    private StructureMapper structureMapper;

    @Resource
    private AlgorithmAssessmentMapper algorithmAssessmentMapper;

    @Resource
    private PathologicalIndicatorCategoryMapper categoryMapper;

    @Resource
    private RemoteLabelService remoteLabelService;
    
    @Resource
    private StructureService structureService;

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

    @Override
    public void examineComparison(Long algorithmJsonId) throws Exception {
        // 查询详情信息
        AlgorithmJson algorithmJsonBy = algorithmJsonMapper.selectById(algorithmJsonId);

        if (algorithmJsonBy == null) {
            throw new Exception(MessageSource.M("NOT_FOND_FILE"));
        }
        // 查询考题详情
        AlgorithmAssessment algorithmAssessment = algorithmAssessmentMapper.selectById(algorithmJsonBy.getAlgorithmAssessmentId());
        if (algorithmAssessment == null) {
            throw new Exception(MessageSource.M("NOT_FOND_ALGORITHM_ASSESSMENT"));
        }
        // 更新当前表中选中状态
        AlgorithmJson algorithmJson = new AlgorithmJson();
        algorithmJson.setAlgorithmJsonId(algorithmJsonId);
        algorithmJson.setSelectedStatus("1");
        algorithmJsonMapper.updateById(algorithmJson);

        // 构建map
        com.alibaba.fastjson2.JSONObject markingJsonObject = new com.alibaba.fastjson2.JSONObject();
        markingJsonObject.put("annotation_json_url", algorithmAssessment.getAnnotationJsonUrl());
        markingJsonObject.put("algorithm_json_url", algorithmJsonBy.getAlgorithmJsonUrl());
        markingJsonObject.put("slide_id", algorithmJsonBy.getSlideId());
        markingJsonObject.put("round_id", algorithmJsonBy.getRoundId());
        markingJsonObject.put("json_name", algorithmJsonBy.getAlgorithmJsonName());
        markingJsonObject.put("create_by", SecurityUtils.getLoginUser().getSysUser().getUserId());
        markingJsonObject.put("algorithm_assessment_id", algorithmJsonBy.getAlgorithmAssessmentId());
        // 调用python接口
        remoteLabelService.algoExamine(markingJsonObject);

    }

    @Override
    public JSONObject getGeoJson(SelectGeoJson selectGeoJson) throws Exception {
        // 获取json列表，判断
        List<Long> algorithmJsonList = selectGeoJson.getAlgorithmJsonIdList();
        if (algorithmJsonList.size() == 1) {
            AlgorithmJson algorithmJson = algorithmJsonMapper.selectById(algorithmJsonList.get(0));
            if (algorithmJson != null) {
                if (Objects.equals(algorithmJson.getJsonType(), "0")) {
                    throw new Exception(MessageSource.M("PROHIBIT_SELECT_ONE_JSON"));
                }
            }
        }
        List<String> labelList = new ArrayList<>();
        // 判断传入列表不为空
        if (selectGeoJson.getLabelList().size() > 0) {
            // 根据labelCode查询标签详情
            for (String labelCode : selectGeoJson.getLabelList()) {
                QueryWrapper<PathologicalIndicatorCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("structure_id", labelCode);
                queryWrapper.eq("organization_id", SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
                queryWrapper.eq("del_flag", 0);
                PathologicalIndicatorCategory pathologicalIndicatorCategory = categoryMapper.selectOne(queryWrapper);

                if (pathologicalIndicatorCategory != null) {
                    QueryWrapper<PathologicalIndicatorCategory> categoryQueryWrapper = new QueryWrapper<>();
                    categoryQueryWrapper.eq("category_code", pathologicalIndicatorCategory.getCategoryCode());
                    List<PathologicalIndicatorCategory> pathologicalIndicatorCategoryList = categoryMapper.selectList(categoryQueryWrapper);
                    for (PathologicalIndicatorCategory pathologicalIndicatorCategory1 : pathologicalIndicatorCategoryList) {
                        labelList.add(pathologicalIndicatorCategory1.getStructureId());
                    }
                }
            }
        }
        QueryWrapper<AlgorithmJson> algorithmJsonQueryWrapper = new QueryWrapper<>();
        algorithmJsonQueryWrapper.in("algorithm_json_id", algorithmJsonList);
        // 查询选中的json列表
        List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(algorithmJsonQueryWrapper);
        // 循环json
        JSONObject resJsonObject = new JSONObject();

        for (AlgorithmJson algorithmJson : algorithmJsons) {
            JSONArray features = new JSONArray();
            // 获取json文件路径
            if (algorithmJson.getAlgorithmJsonUrl() != null) {
                JSONObject jsonObject = getGeoJson(algorithmJson.getAlgorithmJsonUrl());
                if (jsonObject.size() > 0) {
                    JSONArray featuresJson = jsonObject.getJSONArray("features");
                    if (labelList.size() > 0) {
                        featuresJson = featuresJson.stream().filter(s -> labelList.contains(((JSONObject) s).getJSONObject("properties").getString("label_code"))).collect(Collectors.toCollection(JSONArray::new));
                    }
                    features.addAll(updateYs(featuresJson));
                }
            }
            resJsonObject.put(String.valueOf(algorithmJson.getAlgorithmJsonId()), features);
        }
        return resJsonObject;
    }

    @Override
    public SelectGeoJsonList selectUserAndLabelList(SelectGeoJson selectGeoJson) {
        // 获取json列表，判断
        List<Long> algorithmJsonList = selectGeoJson.getAlgorithmJsonIdList();
        QueryWrapper<AlgorithmJson> algorithmJsonQueryWrapper = new QueryWrapper<>();
        algorithmJsonQueryWrapper.in("algorithm_json_id", algorithmJsonList);
        // 查询选中的json列表
        List<AlgorithmJson> algorithmJsons = algorithmJsonMapper.selectList(algorithmJsonQueryWrapper);
        // 循环json
        JSONArray labelInfo = new JSONArray();
        List<Long> userListBy = new ArrayList<>();
        for (AlgorithmJson algorithmJson : algorithmJsons) {
            // 获取json文件路径
            if (algorithmJson.getAlgorithmJsonUrl() != null) {
                JSONObject jsonObject = getGeoJson(algorithmJson.getAlgorithmJsonUrl());
                if (jsonObject.size() > 0) {
                    JSONArray featuresJson = jsonObject.getJSONArray("features");
                    List<Long> userList = featuresJson.stream().map(s -> ((JSONObject) s).getJSONObject("properties").getLong("annotation_owner")).collect(Collectors.toList());
                    userListBy.addAll(userList);
                    JSONArray labelNameJson = jsonObject.getJSONArray("label_info");
                    labelInfo.addAll(labelNameJson);
                }
            }
        }
        // 对结果进行去重
        List<Long> userLists = userListBy.stream().filter(s -> s != 0).distinct().collect(Collectors.toList());
        JSONArray labelInfoList = labelInfo.stream().distinct().collect(Collectors.toCollection(JSONArray::new));
        JSONArray labelInfoLists = new JSONArray();
        // 对标注区域和考核区域进行筛选,只选择code为RO的标签
        if (labelInfoList.size() > 0) {
        	for (Object i : labelInfoList) {
        		JSONObject labelInfos = JSONObject.parseObject(JSONObject.toJSONString(i));
        		String labelCode = labelInfos.getString("label_code");
        		// 根据主键查询详情
        		//Structure structure = structureMapper.selectById(labelCode);
        		List<Structure> structureList = structureService.getListByStructureId(labelCode);
        		if(CollectionUtils.isNotEmpty(structureList)){
        				if (!Objects.equals(structureList.get(0).getType(), "ROE")) {
        					// 查询结果
        					labelInfoLists.add(labelInfos);
        				}
        		}
        	}
        }
        // 封装数据
        SelectGeoJsonList selectGeoJsonList = new SelectGeoJsonList();
        selectGeoJsonList.setUserList(userLists);
        selectGeoJsonList.setLabelInfoList(labelInfoLists);
        return selectGeoJsonList;
    }

    public JSONArray updateYs(JSONArray features) {
        JSONArray jsonArray = new JSONArray();
        for (Object i : features) {
            JSONObject featureObject = (JSONObject) i;
            JSONObject geometry = featureObject.getJSONObject("geometry");
            featureObject.put("geometry", GeometryUtil.updateYAxle(geometry));
            jsonArray.add(featureObject);
        }
        return jsonArray;
    }
}
