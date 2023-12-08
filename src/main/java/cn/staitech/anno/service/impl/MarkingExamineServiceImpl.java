package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.*;
import cn.staitech.anno.mapper.MarkingExamineMapper;
import cn.staitech.anno.mapper.QuestionBankMapper;
import cn.staitech.anno.mapper.QuestionProjectRelMapper;
import cn.staitech.anno.mapper.StructureMapper;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.anno.utils.*;
import cn.staitech.anno.vo.annotation.BroadcastVO;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.marking.MarkingExamineInsertVO;
import cn.staitech.anno.vo.marking.MarkingExamineUpdateVO;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cn.staitech.anno.constant.CommonConstant.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
@Service
public class MarkingExamineServiceImpl extends ServiceImpl<MarkingExamineMapper, MarkingExamine> implements MarkingExamineService {


    @Resource
    private MarkingExamineMapper markingExamineMapper;

    @Resource
    private QuestionProjectRelMapper questionProjectRelMapper;

    @Resource
    private StructureMapper structureMapper;

    @Resource
    private QuestionBankMapper questionBankMapper;

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
    public JSONArray selectQuestionMarkingList(Long questionId) {
        try {
            // 查询题库详情
            QuestionBank questionBank = questionBankMapper.selectById(questionId);
            // 获取json文件路径
            String fileUrl = questionBank.getGeojsonUrl();
            // 取出文件中需要得标注数据
            String fileContent = getAnnotation(fileUrl).getString("features");
            JSONArray jsonArray = JSONArray.parseArray(fileContent);
            JSONArray newJsonArray = new JSONArray();
            for (Object feature : jsonArray) {
                JSONObject featureObject = (JSONObject) feature;
                String labelCode = featureObject.getJSONObject("properties").getString("label_code");
                QueryWrapper<Structure> structureQueryWrapper = new QueryWrapper<>();
                structureQueryWrapper.eq("structure_id", labelCode).eq("type", "ROE");

                Structure structure = structureMapper.selectOne(structureQueryWrapper);
                if (structure != null) {
                    // 获取geometry数据
                    JSONObject geometry = featureObject.getJSONObject("geometry");
                    ((JSONObject) feature).put("geometry", GeometryUtil.updateYAxle(geometry));
                    newJsonArray.add(feature);
                }
            }
            return newJsonArray;
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    @Override
    public List<Features> selectLists(Long questionProjectId, Long createBy) throws Exception {
        MarkingExamine markingExamine = new MarkingExamine();
        markingExamine.setQuestionProjectId(questionProjectId);
        markingExamine.setCreateBy(createBy);
        return markingExamineMapper.selectLists(markingExamine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(MarkingExamineInsertVO req) throws Exception {
        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(req.getQuestion_project_id());
        if (questionProjectRel == null) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }
        MarkingExamine markingExamine = new MarkingExamine();
        BeanUtils.copyProperties(req, markingExamine);

        if (req.getArea() != null) {
            Double area = new Double(req.getArea()) * MICRON;
            markingExamine.setArea(String.valueOf(area));
        }
        if (req.getPerimeter() != null) {
            Double perimeter = new Double(req.getPerimeter()) * MICRON;
            markingExamine.setPerimeter(String.valueOf(perimeter));
        }
        markingExamine.setQuestionProjectId(req.getQuestion_project_id());
        markingExamine.setCreateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        markingExamine.setAnnotationOwner(SecurityUtils.getLoginUser().getSysUser().getUserName());
        markingExamine.setCreateTime(new Date());
        markingExamine.setCategoryId(req.getCategory_id());
        markingExamine.setLocationType(req.getLocation_type());
        // 添加数据库，添加后返回自增id
        markingExamineMapper.insert(markingExamine);
        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = MarkingUtils.socketData("", req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features);
        String questionProjectId = req.getQuestion_project_id() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        return markingExamine.getMarkingExamineId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long markingExamineId) throws Exception {
        if (!Optional.ofNullable(markingExamineId).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(markingExamineId);
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Properties properties = markingExamineMapper.selectBy(markingExamineId);
        Features features = MarkingUtils.socketData("", markingExamineBy.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features);
        String questionProjectId = markingExamineBy.getQuestionProjectId() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        int res = markingExamineMapper.deleteById(markingExamineId);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(MarkingExamineUpdateVO req) throws Exception {
        // 查询标注表中信息
        MarkingExamine markingExamineBy = markingExamineMapper.selectById((req.getMarking_id()));
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        // 查询标注表中信息
        // 更新前数据
        // 更新文件中的内容
        MarkingExamine markingExamine = new MarkingExamine();
        BeanUtils.copyProperties(req, markingExamine);
        markingExamine.setUpdateTime(new Date());
        if (req.getArea() != null) {
            Double area = new Double(req.getArea()) * MICRON;
            markingExamine.setArea(String.valueOf(area));
        }
        if (req.getPerimeter() != null) {
            Double perimeter = new Double(req.getPerimeter()) * MICRON;
            markingExamine.setPerimeter(String.valueOf(perimeter));
        }
        markingExamine.setMarkingExamineId(req.getMarking_id());
        markingExamine.setUpdateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        markingExamine.setAnnotationOwner(SecurityUtils.getLoginUser().getSysUser().getUserName());
        markingExamine.setUpdateTime(new Date());
        markingExamine.setCategoryId(req.getCategory_id());
        markingExamineMapper.updateById(markingExamine);
        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = MarkingUtils.socketData("", req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features);
        // 使用websocket发送数据
        String questionProjectId = markingExamineBy.getQuestionProjectId() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        return markingExamine.getMarkingExamineId();
    }


    @Override
    public JSONObject updateOperation(UpdateOperationIn req) throws Exception {
        MarkingExamine markingExamineBy = markingExamineMapper.selectById((req.getMarking_id()));
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        Marking marking = MarkingUtils.updateVerify(markingExamineBy.getGeometry(),req.getGeometry(),req.getOperation(),req.getCheck(), Double.parseDouble(req.getResolution()));
        JSONObject jsonObject = JSONObject.parseObject(WktUtil.wktToJson(marking.getMarkingId()));
        MarkingExamine markingExamine = new MarkingExamine();
        markingExamine.setGeometry(jsonObject);
        markingExamine.setArea(marking.getArea());
        markingExamine.setPerimeter(marking.getPerimeter());
        markingExamine.setMarkingExamineId(Long.valueOf(req.getMarking_id()));
        markingExamine.setUpdateBy(SecurityUtils.getUserId());
        markingExamine.setUpdateTime(new Date());
        markingExamineMapper.updateById(markingExamine);
        // 更新后查询数据并返回
        Properties properties = markingExamineMapper.selectBy(Long.valueOf(req.getMarking_id()));
        Features features = MarkingUtils.socketData("", markingExamine.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features);
        String questionProjectId = markingExamineBy.getQuestionProjectId() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        return markingExamine.getGeometry();
    }

    @Override
    public double operationCheck(UpdateOperationIn req) throws Exception {
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(req.getMarking_id());
        // 查询数据是否存在
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        return MarkingUtils.updateOperationVerify(markingExamineBy.getGeometry(),req.getGeometry(),req.getOperation());
    }

    public JSONObject getAnnotation(String fileUrl) {
        JSONObject parse = new JSONObject();
        File jsonFile = new File(fileUrl);
        if (jsonFile.exists()) {
            //通过getStr方法获取json文件的内容
            String jsonData = getStr(jsonFile);
            //转json对象
            parse = (JSONObject) JSONObject.parse(jsonData);
            return parse;
        }
        return parse;
    }

}
