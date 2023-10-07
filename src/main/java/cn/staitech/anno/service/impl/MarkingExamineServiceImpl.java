package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.MarkingExamine;
import cn.staitech.anno.domain.QuestionProjectRel;
import cn.staitech.anno.domain.geojson.Features;
import cn.staitech.anno.domain.geojson.Properties;
import cn.staitech.anno.domain.markingExamine.MarkingExamineInsertVO;
import cn.staitech.anno.domain.markingExamine.MarkingExamineUpdateVO;
import cn.staitech.anno.domain.vo.BroadcastVO;
import cn.staitech.anno.mapper.MarkingExamineMapper;
import cn.staitech.anno.mapper.QuestionProjectRelMapper;
import cn.staitech.anno.netty.websocket.NioWebSocketHandler;
import cn.staitech.anno.project.mapper.SlideMapperV1;
import cn.staitech.anno.service.MarkingExamineService;
import cn.staitech.anno.utils.SendMessage;
import cn.staitech.common.core.utils.bean.BeanUtils;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static cn.staitech.anno.constant.AnnotationConstant.*;
import static cn.staitech.anno.constant.ViewerConstant.MICRON;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gjt
 * @since 2023-09-25
 */
@Service
public class MarkingExamineServiceImpl extends ServiceImpl<MarkingExamineMapper, MarkingExamine> implements MarkingExamineService {

    @Resource
    private SlideMapperV1 slideMapperV1;

    @Resource
    private MarkingExamineMapper markingExamineMapper;

    @Resource
    private QuestionProjectRelMapper questionProjectRelMapper;

    @Resource
    private MarkingServiceImpl markingServiceImpl;

    @Override
    public List<Features> selectLists(Long questionProjectId) throws Exception {
        return markingExamineMapper.selectLists(questionProjectId);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(MarkingExamineInsertVO req) throws Exception {
        QuestionProjectRel questionProjectRel = questionProjectRelMapper.selectById(req.getQuestion_project_id());
        if (questionProjectRel == null) {
            throw new Exception("未查询到切片信息");
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
        markingExamine.setLocationType(req.getLocation_type());
        // 添加数据库，添加后返回自增id
        markingExamineMapper.insert(markingExamine);
        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = markingServiceImpl.socketData("", req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(ADD_STATUS, features);
        NioWebSocketHandler.sendAll(req.getQuestion_project_id(), broadcastVO);
        return markingExamine.getMarkingExamineId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long markingExamineId) throws Exception {
        if (!Optional.ofNullable(markingExamineId).isPresent()) {
            throw new Exception("参数异常");
        }
        MarkingExamine markingExamineBy = markingExamineMapper.selectById(markingExamineId);
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception("未查询到标注信息");
        }
        Properties properties = markingExamineMapper.selectBy(markingExamineId);
        Features features = markingServiceImpl.socketData("", markingExamineBy.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(DELETE_STATUS, features);
        NioWebSocketHandler.sendAll(markingExamineBy.getQuestionProjectId(), broadcastVO);
        int res = markingExamineMapper.deleteById(markingExamineId);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(MarkingExamineUpdateVO req) throws Exception {
        // 查询标注表中信息
        MarkingExamine markingExamineBy = markingExamineMapper.selectById((req.getMarking_id()));
        if (!Optional.ofNullable(markingExamineBy).isPresent()) {
            throw new Exception("未查询到标注信息");
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
        markingExamine.setUpdateBy(SecurityUtils.getLoginUser().getSysUser().getUserId());
        markingExamine.setAnnotationOwner(SecurityUtils.getLoginUser().getSysUser().getUserName());
        markingExamine.setUpdateTime(new Date());
        markingExamineMapper.updateById(markingExamine);
        Properties properties = markingExamineMapper.selectBy(markingExamine.getMarkingExamineId());
        Features features = markingServiceImpl.socketData("", req.getGeometry(), properties);
        BroadcastVO broadcastVO = SendMessage.sendOneMessages(UPDATE_STATUS, features);
        // 使用websocket发送数据
        NioWebSocketHandler.sendAll(markingExamine.getQuestionProjectId(), broadcastVO);
        return markingExamine.getMarkingExamineId();
    }

}
