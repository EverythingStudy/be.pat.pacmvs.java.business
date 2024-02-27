package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.*;
import cn.staitech.anno.domain.history.Session;
import cn.staitech.anno.domain.history.Trace;
import cn.staitech.anno.domain.history.TraceNode;
import cn.staitech.anno.mapper.*;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.domain.Marking;
import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.anno.utils.*;
import cn.staitech.anno.vo.annotation.BroadcastVO;
import cn.staitech.anno.vo.geojson.Features;
import cn.staitech.anno.vo.geojson.Properties;
import cn.staitech.anno.vo.geojson.in.UpdateOperationIn;
import cn.staitech.anno.vo.geojson.out.BatchResult;
import cn.staitech.anno.vo.history.HistoryDTO;
import cn.staitech.anno.vo.marking.MarkingExamineInsertVO;
import cn.staitech.anno.vo.marking.MarkingMerge;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.overlay.OverlayOp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static cn.staitech.anno.constant.CommonConstant.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
@Slf4j
@Service
public class MarkingExamineServiceImpl extends ServiceImpl<MarkingExamineMapper, MarkingExamine> implements MarkingExamineService {


    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);
    private static final WKTReader WKT_READER = new WKTReader(GEOMETRY_FACTORY);
    @Resource
    private MarkingExamineMapper markingExamineMapper;
    @Resource
    private QuestionProjectRelMapper questionProjectRelMapper;
    @Resource
    private StructureMapper structureMapper;
    @Resource
    private ImageMapper imageMapper;
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
                structureQueryWrapper.eq("organization_id", SecurityUtils.getLoginUser().getSysUser().getOrganizationId());

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

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();
        String userName = sysUser.getUserName();
        Long slideId = req.getSlide_id();

        MarkingExamine markingExamine = new MarkingExamine();
        BeanUtils.copyProperties(req, markingExamine);
        markingExamine.setPerimeter(req.getArea());
        markingExamine.setPerimeter(req.getPerimeter());
        markingExamine.setQuestionProjectId(req.getQuestion_project_id());
        markingExamine.setCreateBy(userId);
        markingExamine.setAnnotationOwner(userName);
        markingExamine.setCreateTime(new Date());
        markingExamine.setCategoryId(req.getCategory_id());
        markingExamine.setLocationType(req.getLocation_type());
        // 添加数据库，添加后返回自增id
        markingExamineMapper.insert(markingExamine);
        Long markingExamineId = markingExamine.getMarkingExamineId();

        Properties properties = markingExamineMapper.selectBy(markingExamineId);
        Features features = MarkingUtils.socketData("", req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features);
        String questionProjectId = req.getQuestion_project_id() + GLIDE_LINE + userId;
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);

        {
            String traceId = req.getTraceId();
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            // 2、创建Trace,并存入Session.list,LinkedList<Trace>
            // 单条记录
            Trace trace = new Trace(userId, traceId, req.getIsBatch());
            // 批量操作
            if (req.getIsBatch() && session.getTraceById(traceId) != null) {
                // 若trace已经存在，不用再add
                trace = session.getTraceById(traceId);
                trace.getNodeList().add(new TraceNode(markingExamineId.toString(), "INSERT"));
            } else {
                trace.getNodeList().add(new TraceNode(markingExamineId.toString(), "INSERT"));
                // session.addTrace(trace, false, false);
                session.add(trace);
            }

            // 3、数据持久化写入RocksDB
            Gson gson = new Gson();
            // 将对象转换成JSON字符串
            // String json = gson.toJson(markingExamine);
            String json = gson.toJson(markingExamine);
            RocksDBUtil.put(traceId, markingExamineId.toString(), json);
        }

        return markingExamineId;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertByHistory(MarkingExamine markingExamine, String traceId, Boolean isBatch, Boolean isUndo) throws Exception {
        Long beforeMarkingExamineId = markingExamine.getMarkingExamineId();
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();

        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(markingExamine.getQuestionProjectId());
        if (questionProjectRel == null) {
            throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
        }

        // 查询slideId
        Long questionId = questionProjectRel.getQuestionId();
        QuestionBank questionBank = questionBankMapper.selectById(questionId);
        Long slideId = questionBank.getSlideId();

        // 添加数据库，添加后返回自增id
        markingExamineMapper.insert(markingExamine);
        // Long markingExamineId = markingExamine.getMarkingExamineId();

        // 更新数据 - 查新增的，取新ID
        //MarkingExamine markingExamineNew = markingExamineMapper.selectById(markingExamine.getMarkingExamineId());

        {
            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            LinkedList<Trace> drawList = session.getDrawList();
            LinkedList<Trace> undoList = session.getUndoList();

            if (isUndo) {
                if (!drawList.isEmpty()) {
                    Trace trace = drawList.get(drawList.size() - 1);
                    trace.setTraceId(traceId);

                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingExamineId.toString())) {
                            traceNode.setId(markingExamine.getMarkingExamineId().toString());
                        }
                    }

                    undoList.add(trace);

                    renewId(drawList, beforeMarkingExamineId, markingExamine.getMarkingExamineId());
                    renewId(undoList, beforeMarkingExamineId, markingExamine.getMarkingExamineId());

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamine);
                    RocksDBUtil.put(traceId, markingExamine.getMarkingExamineId().toString(), json);

                    // undoList.add(drawList.get(drawList.size() - 1));
                    drawList.remove(drawList.size() - 1);
                }
            } else {
                if (!undoList.isEmpty()) {

                    Trace trace = undoList.get(undoList.size() - 1);
                    trace.setTraceId(traceId);

                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingExamineId.toString())) {
                            traceNode.setId(markingExamine.getMarkingExamineId().toString());
                        }
                    }

                    drawList.add(trace);

                    renewId(drawList, beforeMarkingExamineId, markingExamine.getMarkingExamineId());
                    renewId(undoList, beforeMarkingExamineId, markingExamine.getMarkingExamineId());

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamine);
                    RocksDBUtil.put(traceId, markingExamine.getMarkingExamineId().toString(), json);

                    //drawList.add(undoList.get(undoList.size() - 1));
                    undoList.remove(undoList.size() - 1);
                }
            }
        }

        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = MarkingUtils.socketData("", markingExamine.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features);
        String questionProjectId = markingExamine.getQuestionProjectId() + GLIDE_LINE + userId;
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);

        return markingExamine.getMarkingExamineId();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long markingExamineId, String traceId, Boolean isBatch) throws Exception {
        if (!Optional.ofNullable(markingExamineId).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(markingExamineId);
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();
        Long questionProjectId = markingExamineBy.getQuestionProjectId();

        Properties properties = markingExamineMapper.selectBy(markingExamineId);
        Features features = MarkingUtils.socketData("", markingExamineBy.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features);
        String questionProjectIdString = questionProjectId + GLIDE_LINE + userId;
        NioWebSocketHandler.sendQuestionProject(questionProjectIdString, broadcastVO);
        int res = markingExamineMapper.deleteById(markingExamineId);

        {
            QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(questionProjectId);
            if (questionProjectRel == null) {
                throw new Exception(MessageSource.M("NO_SLIDE_DATA"));
            }

            // 查询slideId
            Long questionId = questionProjectRel.getQuestionId();
            QuestionBank questionBank = questionBankMapper.selectById(questionId);
            Long slideId = questionBank.getSlideId();

            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            // 2、创建Trace,并存入Session.list,LinkedList<Trace>
            // 单条记录
            Trace trace = new Trace(userId, traceId, isBatch);
            // 批量操作

            if (isBatch && session.getTraceById(traceId) != null) {
                // 若trace已经存在，不用再add
                trace = session.getTraceById(traceId);
                trace.getNodeList().add(new TraceNode(markingExamineId.toString(), "DELETE"));
            } else {
                trace.getNodeList().add(new TraceNode(markingExamineId.toString(), "DELETE"));
                //session.addTrace(trace, isHistory, isUndo);
                session.add(trace);
            }

            // 3、数据持久化写入RocksDB
            Gson gson = new Gson();
            // 将对象转换成JSON字符串
            String json = gson.toJson(markingExamineBy);
            RocksDBUtil.put(traceId, markingExamineId.toString(), json);
        }

        return res;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByHistory(Long markingExamineId, String traceId, Boolean isBatch, Boolean isUndo) throws Exception {
        Long beforeMarkingExamineId = markingExamineId;
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(markingExamineId);
        Long questionProjectId = markingExamineBy.getQuestionProjectId();

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();

        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(questionProjectId);
        // 查询slideId
        Long questionId = questionProjectRel.getQuestionId();
        QuestionBank questionBank = questionBankMapper.selectById(questionId);
        Long slideId = questionBank.getSlideId();

        {
            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            LinkedList<Trace> drawList = session.getDrawList();
            LinkedList<Trace> undoList = session.getUndoList();

            if (isUndo) {
                if (!drawList.isEmpty()) {
                    Trace trace = drawList.get(drawList.size() - 1);
                    trace.setTraceId(traceId);

                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingExamineId.toString())) {
                            traceNode.setId(markingExamineBy.getMarkingExamineId().toString());
                        }
                    }

                    undoList.add(trace);

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamineBy);
                    RocksDBUtil.put(traceId, markingExamineBy.getMarkingExamineId().toString(), json);

                    // undoList.add(drawList.get(drawList.size() - 1));
                    drawList.remove(drawList.size() - 1);
                }
            } else {
                if (!undoList.isEmpty()) {
                    drawList.add(undoList.get(undoList.size() - 1));
                    undoList.remove(undoList.size() - 1);
                }
            }
        }

        Properties properties = markingExamineMapper.selectBy(markingExamineId);
        Features features = MarkingUtils.socketData("", markingExamineBy.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features);
        String questionProjectIdString = questionProjectId + GLIDE_LINE + userId;
        NioWebSocketHandler.sendQuestionProject(questionProjectIdString, broadcastVO);
        int res = markingExamineMapper.deleteById(markingExamineId);
        return res;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(MarkingExamineInsertVO req) throws Exception {
        String markingId = req.getMarking_id();
        // 查询标注表中信息
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(markingId);
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }

        String traceId = req.getTraceId();
        Boolean isBatch = req.getIsBatch();
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();

        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(markingExamineBy.getQuestionProjectId());
        // 查询slideId
        Long questionId = questionProjectRel.getQuestionId();
        QuestionBank questionBank = questionBankMapper.selectById(questionId);
        Long slideId = questionBank.getSlideId();


        {
            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            // 2、创建Trace,并存入Session.list,LinkedList<Trace>
            // 单条记录
            Trace trace = new Trace(userId, traceId, isBatch);
            // 批量操作

            if (isBatch && session.getTraceById(traceId) != null) {
                // 若trace已经存在，不用再add
                trace = session.getTraceById(traceId);
                trace.getNodeList().add(new TraceNode(markingId, "UPDATE"));
            } else {
                trace.getNodeList().add(new TraceNode(markingId, "UPDATE"));
                //session.addTrace(trace, false, false);
                session.add(trace);
            }

            // 3、数据持久化写入RocksDB
            Gson gson = new Gson();
            // 将对象转换成JSON字符串
            String json = gson.toJson(markingExamineBy);
            RocksDBUtil.put(traceId, markingId, json);
        }

        // 查询标注表中信息
        // 更新前数据
        // 更新文件中的内容
        MarkingExamine markingExamine = new MarkingExamine();
        BeanUtils.copyProperties(req, markingExamine);
        markingExamine.setUpdateTime(new Date());
        markingExamine.setArea(req.getArea());
        markingExamine.setPerimeter(req.getPerimeter());
        markingExamine.setMarkingExamineId(Long.valueOf(req.getMarking_id()));
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
    @Transactional(rollbackFor = Exception.class)
    public Long updateByHistory(MarkingExamine req, String traceId, Boolean isBatch, Boolean isUndo) throws Exception {
        Long beforeMarkingId = req.getMarkingExamineId();
        // 查询标注表中信息
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(beforeMarkingId);
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }

        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();

        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(markingExamineBy.getQuestionProjectId());
        // 查询slideId
        Long questionId = questionProjectRel.getQuestionId();
        QuestionBank questionBank = questionBankMapper.selectById(questionId);
        Long slideId = questionBank.getSlideId();

        {
            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);


            LinkedList<Trace> drawList = session.getDrawList();
            LinkedList<Trace> undoList = session.getUndoList();

            if (isUndo) {
                if (!drawList.isEmpty()) {
                    Trace trace = drawList.get(drawList.size() - 1);
                    trace.setTraceId(traceId);

                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingId.toString())) {
                            traceNode.setId(beforeMarkingId.toString());
                        }
                    }

                    undoList.add(trace);

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamineBy);
                    RocksDBUtil.put(traceId, beforeMarkingId.toString(), json);

                    // undoList.add(drawList.get(drawList.size() - 1));
                    drawList.remove(drawList.size() - 1);
                }
            } else {
                if (!undoList.isEmpty()) {

                    Trace trace = undoList.get(undoList.size() - 1);
                    trace.setTraceId(traceId);

                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingId.toString())) {
                            traceNode.setId(beforeMarkingId.toString());
                        }
                    }

                    drawList.add(trace);

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamineBy);
                    RocksDBUtil.put(traceId, beforeMarkingId.toString(), json);

                    //drawList.add(undoList.get(undoList.size() - 1));
                    undoList.remove(undoList.size() - 1);
                }
            }
        }

        markingExamineBy.setGeometry(req.getGeometry());
        markingExamineMapper.updateById(markingExamineBy);
        Properties properties = markingExamineMapper.selectBy(markingExamineBy.getMarkingExamineId());
        Features features = MarkingUtils.socketData("", req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features);
        // 使用websocket发送数据
        String questionProjectId = markingExamineBy.getQuestionProjectId() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        return markingExamineBy.getMarkingExamineId();
    }


    @Override
    public int padding(Long markingId) throws Exception {
        MarkingExamine markingBy = markingExamineMapper.selectById(markingId);
        if (!Optional.ofNullable(markingBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        JSONObject geometryJson = MarkingUtils.padding(markingBy.getGeometry());
        MarkingExamine markingExamine = new MarkingExamine();
        markingExamine.setGeometry(geometryJson);
        Geometry geometry = WKT_READER.read(WktUtil.jsonToWkt(markingExamine.getGeometry()));
        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(markingBy.getQuestionProjectId());
        QuestionBank questionBank = questionBankMapper.selectById(questionProjectRel.getQuestionId());
        Image image = imageMapper.selectById(questionBank.getImageId());
        if (image.getResolutionX() != null) {
            double resolutions = Double.parseDouble(image.getResolutionX());
            String area = String.valueOf(geometry.getArea() * resolutions * resolutions);
            markingExamine.setArea(area);
            String per = String.valueOf(geometry.getLength() * resolutions);
            markingExamine.setPerimeter(per);
        }
        markingExamine.setMarkingExamineId(markingId);
        markingExamine.setUpdateTime(new Date());
        markingExamine.setUpdateBy(SecurityUtils.getUserId());
        int res = markingExamineMapper.updateById(markingExamine);
        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = MarkingUtils.socketData("", geometryJson, properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features);
        // 使用websocket发送数据
        String questionProjectId = markingBy.getQuestionProjectId() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        return res;
    }

    @Override
    public int stickup(String markingId) {
        MarkingExamine markingExamine = markingExamineMapper.selectById(markingId);
        markingExamine.setCreateTime(new Date());
        int res = markingExamineMapper.insert(markingExamine);
        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = MarkingUtils.socketData("", markingExamine.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features);
        // 使用websocket发送数据
        String questionProjectId = markingExamine.getQuestionProjectId() + GLIDE_LINE + SecurityUtils.getLoginUser().getSysUser().getUserId();
        NioWebSocketHandler.sendQuestionProject(questionProjectId, broadcastVO);
        return res;
    }

    @Override
    public JSONObject markingMerge(MarkingMerge req) throws ParseException {
        QueryWrapper<MarkingExamine> markingQueryWrapper = new QueryWrapper<>();
        markingQueryWrapper.in("marking_id", req.getMarkingIdList());
        List<MarkingExamine> markingList = markingExamineMapper.selectList(markingQueryWrapper);
        List<Geometry> geometryList = new ArrayList<>();
        for (MarkingExamine marking : markingList) {
            Geometry geometry = WKT_READER.read(WktUtil.jsonToWkt(marking.getGeometry()));
            geometryList.add(geometry);
        }
        if (geometryList.size() > 0) {
            if (geometryList.size() == 1) {
                return markingList.get(0).getGeometry();
            }
            Geometry geometry = null;
            for (int i = 0; i < geometryList.size(); i++) {
                if (i == 0) {
                    geometry = geometryList.get(i);
                }
                Geometry geometryIntersection = geometry.intersection(geometryList.get(i));
                if (geometryIntersection.isEmpty()) {
                    return null;
                } else {
                    OverlayOp op = new OverlayOp(geometry, geometryList.get(i));
                    int code = OverlayOp.UNION;
                    geometry = op.getResultGeometry(code);
//                    geometry = geometry.union(geometryList.get(i));
                }
            }
            return JSONObject.parseObject(WktUtil.wktToJson(String.valueOf(geometry)));
        } else {
            return null;
        }
    }


    @Override
    public JSONObject updateOperation(UpdateOperationIn req, String traceId, Boolean isBatch) throws Exception {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long userId = sysUser.getUserId();

        MarkingExamine markingExamineBy = markingExamineMapper.selectById((req.getMarking_id()));
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }

        {
            String markingId = markingExamineBy.getMarkingExamineId().toString();
            QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(markingExamineBy.getQuestionProjectId());
            // 查询slideId
            Long questionId = questionProjectRel.getQuestionId();
            QuestionBank questionBank = questionBankMapper.selectById(questionId);
            Long slideId = questionBank.getSlideId();


            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            // 2、创建Trace,并存入Session.list,LinkedList<Trace>
            // 单条记录
            Trace trace = new Trace(userId, traceId, isBatch);
            // 批量操作

            if (isBatch && session.getTraceById(traceId) != null) {
                // 若trace已经存在，不用再add
                trace = session.getTraceById(traceId);
                trace.getNodeList().add(new TraceNode(markingId, "UPDATEOPERATION"));
            } else {
                trace.getNodeList().add(new TraceNode(markingId, "UPDATEOPERATION"));
                session.add(trace);
            }

            // 3、数据持久化写入RocksDB
            Gson gson = new Gson();
            // 将对象转换成JSON字符串
            String json = gson.toJson(markingExamineBy);
            RocksDBUtil.put(traceId, markingId, json);
        }


        Marking marking = MarkingUtils.updateVerify(markingExamineBy.getGeometry(), req.getGeometry(), req.getOperation(), req.getCheck(), req.getResolution());
        JSONObject jsonObject = JSONObject.parseObject(WktUtil.wktToJson(marking.getMarkingId()));
        MarkingExamine markingExamine = new MarkingExamine();
        markingExamine.setGeometry(jsonObject);
        markingExamine.setArea(marking.getArea());
        markingExamine.setPerimeter(marking.getPerimeter());
        markingExamine.setMarkingExamineId(Long.valueOf(req.getMarking_id()));
        markingExamine.setUpdateBy(userId);
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
    public JSONObject updateOperationByHistory(MarkingExamine req, String traceId, Boolean isBatch, Boolean isUndo) throws Exception {
        Long beforeMarkingId = req.getMarkingExamineId();
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(req.getMarkingExamineId());
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }

        {

            Long userId = markingExamineBy.getCreateBy();
            QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(markingExamineBy.getQuestionProjectId());
            // 查询slideId
            Long questionId = questionProjectRel.getQuestionId();
            QuestionBank questionBank = questionBankMapper.selectById(questionId);
            Long slideId = questionBank.getSlideId();

            // 删除操作RocksDB存删除前的数据
            // 撤消,恢复历史记录 用HistoryService会引起循环依赖！ -> 后续在线程池中处理 判断是批处理，还是单独处理
            // 1、创建Session,并存入ConcurrentHashMap<Long, Session>
            Session session = new Session(userId, slideId);
            String key = userId + "_" + slideId;
            if (!HistoryServiceImpl.USER_SESSION_MAP.containsKey(key)) {
                HistoryServiceImpl.USER_SESSION_MAP.put(key, session);
            }
            session = HistoryServiceImpl.USER_SESSION_MAP.get(key);

            LinkedList<Trace> drawList = session.getDrawList();
            LinkedList<Trace> undoList = session.getUndoList();

            if (isUndo) {
                if (!drawList.isEmpty()) {
                    Trace trace = drawList.get(drawList.size() - 1);
                    trace.setTraceId(traceId);

                    // 防址ID变更，更新切片ID
                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingId.toString())) {
                            traceNode.setId(beforeMarkingId.toString());
                        }
                    }

                    undoList.add(trace);

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamineBy);
                    RocksDBUtil.put(traceId, beforeMarkingId.toString(), json);

                    // undoList.add(drawList.get(drawList.size() - 1));
                    drawList.remove(drawList.size() - 1);
                }
            } else {
                if (!undoList.isEmpty()) {

                    Trace trace = undoList.get(undoList.size() - 1);
                    trace.setTraceId(traceId);

                    for (TraceNode traceNode : trace.getNodeList()) {
                        if (traceNode.getId().equals(beforeMarkingId.toString())) {
                            traceNode.setId(beforeMarkingId.toString());
                        }
                    }

                    drawList.add(trace);

                    // 3、数据持久化写入RocksDB
                    Gson gson = new Gson();
                    // 将对象转换成JSON字符串
                    String json = gson.toJson(markingExamineBy);
                    RocksDBUtil.put(traceId, beforeMarkingId.toString(), json);

                    //drawList.add(undoList.get(undoList.size() - 1));
                    undoList.remove(undoList.size() - 1);
                }
            }
        }

        MarkingExamine markingExamine = new MarkingExamine();
        markingExamine.setGeometry(req.getGeometry());
        markingExamine.setArea(req.getArea());
        markingExamine.setPerimeter(req.getPerimeter());
        markingExamine.setMarkingExamineId(Long.valueOf(req.getMarkingExamineId()));
        markingExamine.setUpdateBy(SecurityUtils.getUserId());
        markingExamine.setUpdateTime(new Date());
        markingExamineMapper.updateById(markingExamine);
        // 更新后查询数据并返回
        Properties properties = markingExamineMapper.selectBy(Long.valueOf(req.getMarkingExamineId()));
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
        return MarkingUtils.updateOperationVerify(markingExamineBy.getGeometry(), req.getGeometry(), req.getOperation());
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

    /**
     * 批量处理
     *
     * @param list
     * @return
     */
    public List<BatchResult> batch(List<MarkingExamineInsertVO> list) {
        List<BatchResult> result = new ArrayList<>(list.size());
        String traceId = UUID.randomUUID().toString();

        for (MarkingExamineInsertVO dto : list) {
            dto.setTraceId(traceId);
            dto.setIsBatch(true);

            BatchResult batchResult = new BatchResult();
            batchResult.setFront_id(dto.getMarking_id().toString());
            try {
                switch (dto.getOperation()) {
                    case "INSERT":
                        Long markingIdIns = insert(dto);
                        if (markingIdIns > 0) {
                            batchResult.setData(markingIdIns.toString());
                            break;
                        }
                    case "DELETE":
                        if (delete(Long.valueOf(dto.getMarking_id()), traceId, true) > 0) {
                            batchResult.setData(dto.getMarking_id().toString());
                            break;
                        }
                    case "UPDATE":
                        Long markingId = update(dto);
                        if (markingId > 0) {
                            batchResult.setData(markingId.toString());
                            break;
                        }
                    default:
                }

                batchResult.setStatus(true);
                batchResult.setMessage(MessageSource.M("OPERATE_SUCCEED"));
            } catch (Exception e) {
                batchResult.setData(dto.getMarking_id().toString());
                batchResult.setMessage(e.getMessage());
                batchResult.setStatus(false);
            }

            result.add(batchResult);
        }
        return result;
    }


    @Override
    public Boolean process(HistoryDTO dto) {
        try {
            switch (dto.getEnvType()) {
                case 1:
                    undo(dto);
                    break;
                case 2:
                    redo(dto);
                    break;
                default:
            }

        } catch (Exception e) {

        }
        return true;
    }


    @Override
    public Boolean undo(HistoryDTO dto) {

        String traceId = UUID.randomUUID().toString();
        // Boolean isUndo = dto.getEnvType() == 1 ? true : false;
        Boolean isUndo = true;

        String key = dto.getUserId() + "_" + dto.getSlideId();
        Session session = HistoryServiceImpl.USER_SESSION_MAP.get(key);
        LinkedList<Trace> drawList = session.getDrawList();

        if (!drawList.isEmpty()) {
            Trace trace = drawList.get(drawList.size() - 1);
            Boolean isBatch = trace.getIsBatch();

            List<TraceNode> traceNodeList = trace.getNodeList();

            for (int i = traceNodeList.size() - 1; i >= 0; i--) {
                TraceNode node = traceNodeList.get(i);
                String markingId = node.getId();
                try {
                    Gson gson = new Gson();
                    String json = RocksDBUtil.get(trace.getTraceId(), markingId);
                    MarkingExamine markingExamine = gson.fromJson(json, MarkingExamine.class);

                    switch (node.getOperation()) {
                        case "INSERT":
                            deleteByHistory(Long.valueOf(markingId), traceId, isBatch, isUndo);
                            break;
                        case "DELETE":
                            insertByHistory(markingExamine, traceId, isBatch, isUndo);
                            break;
                        case "UPDATE":
                            updateByHistory(markingExamine, traceId, isBatch, isUndo);
                            break;
                        case "UPDATEOPERATION":
                            updateOperationByHistory(markingExamine, traceId, isBatch, isUndo);
                            break;
                        default:
                    }

                } catch (Exception e) {

                }
            }
        }


        return true;
    }


    @Override
    public Boolean redo(HistoryDTO dto) {
        String traceId = UUID.randomUUID().toString();
        // Boolean isUndo = dto.getEnvType() == 1 ? true : false;
        Boolean isUndo = false;

        String key = dto.getUserId() + "_" + dto.getSlideId();
        Session session = HistoryServiceImpl.USER_SESSION_MAP.get(key);
        LinkedList<Trace> undoList = session.getUndoList();


        if (!undoList.isEmpty()) {
            Trace trace = undoList.get(undoList.size() - 1);
            Boolean isBatch = trace.getIsBatch();
            List<TraceNode> traceNodeList = trace.getNodeList();

            for (int i = traceNodeList.size() - 1; i >= 0; i--) {
                TraceNode node = traceNodeList.get(i);
                String markingId = node.getId();
                try {
                    Gson gson = new Gson();
                    String json = RocksDBUtil.get(trace.getTraceId(), markingId);
                    MarkingExamine markingExamine = gson.fromJson(json, MarkingExamine.class);

                    switch (node.getOperation()) {
                        case "INSERT":
                            insertByHistory(markingExamine, traceId, isBatch, isUndo);
                            break;
                        case "DELETE":
                            deleteByHistory(Long.valueOf(markingId), traceId, isBatch, isUndo);
                            break;
                        case "UPDATE":
                            updateByHistory(markingExamine, traceId, isBatch, isUndo);
                            break;
                        case "UPDATEOPERATION":
                            updateOperationByHistory(markingExamine, traceId, isBatch, isUndo);
                            break;
                        default:
                    }

                } catch (Exception e) {

                }
            }
        }


        return true;
    }


    public void renewId(LinkedList<Trace> list, Long oldId, Long newId) {
        if (!list.isEmpty()) {

            for (Trace trace : list) {
                List<TraceNode> traceNodeList = trace.getNodeList();
                for (TraceNode traceNode : traceNodeList) {
                    String oldNodeId = traceNode.getId();
                    try {
                        if (oldNodeId.equals(oldId.toString())) {
                            traceNode.setId(newId.toString());

                            Gson gson = new Gson();
                            String json = RocksDBUtil.get(trace.getTraceId(), oldNodeId);
                            MarkingExamine markingExamine = gson.fromJson(json, MarkingExamine.class);
                            markingExamine.setMarkingExamineId(newId);

                            json = gson.toJson(markingExamine);
                            RocksDBUtil.put(trace.getTraceId(), markingExamine.getMarkingExamineId().toString(), json);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

    }
}
