package cn.staitech.annotation.service.impl;

import cn.staitech.annotation.constant.Constant;
import cn.staitech.annotation.netty.websocket.NioWebSocketHandler;
import cn.staitech.annotation.utils.MessageSource;
import cn.staitech.annotation.utils.measure.MeasureMessageGenerator;
import cn.staitech.annotation.vo.measure.MeasureVo;
import cn.staitech.common.core.domain.R;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.RemoteUserService;
import cn.staitech.system.api.domain.SysUser;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.staitech.annotation.domain.Measure;
import cn.staitech.annotation.service.MeasureService;
import cn.staitech.annotation.mapper.MeasureMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mugw
 * @version 1.0
 * @description 测量标注管理
 * @date 2025/5/21 14:33:43
 */
@Service
public class MeasureServiceImpl extends ServiceImpl<MeasureMapper, Measure>
    implements MeasureService{

    @Resource
    private NioWebSocketHandler webSocketHandler;
    @Resource
    private HttpServletResponse response;
    @Resource
    private RemoteUserService remoteUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<Measure> addMeasure(Measure req) throws Exception {

        if (req == null||req.getGeometry()==null||!req.getGeometry().isSimple()){
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }

        req.setCreateBy(SecurityUtils.getUserId());
        req.setAnnotationType("Measure");
        long number = 1L;
        Measure measure = getOne(Wrappers.<Measure>lambdaQuery().eq(Measure::getSlideId, req.getSlideId()).eq(Measure::getMeasureName, req.getMeasureName())
                .orderBy(false,false,Measure::getMeasureId).last("limit 1"));
        if (measure != null && measure.getNumber() != null) {
            number += measure.getNumber();
        }
        String measureFullName = req.getMeasureName() + number;
        req.setMeasureFullName(measureFullName);
        req.setNumber(number);
        baseMapper.insert(req);
        webSocketHandler.sendMessage(MeasureMessageGenerator.generateAnnotationMessage(req, Constant.ANNO_ACTION_ADD));
        return R.ok(req);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R delete(Long measureId) throws Exception {
        if (!Optional.ofNullable(measureId).isPresent()) {
            throw new Exception(MessageSource.M("ARGUMENT_INVALID"));
        }
        Measure measure = baseMapper.selectById(measureId);
        if (!Optional.ofNullable(measure).isPresent()) {
            throw new Exception(MessageSource.M("NO_ANNOTATION_DATA"));
        }
        baseMapper.deleteById(measureId);
        webSocketHandler.sendMessage(MeasureMessageGenerator.generateAnnotationMessage(measure, Constant.ANNO_ACTION_ADD));
        return R.ok(null, MessageSource.M("OPERATE_SUCCEED"));
    }

    @Override
    public void export(Long slideId) throws Exception {
        List<Measure> measureList = list(Wrappers.<Measure>lambdaQuery().eq(Measure::getSlideId, slideId)
                .ne(Measure::getLocationType, Geometry.TYPENAME_POINT));
        List<MeasureVo> measureVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(measureList)){
            measureVoList = measureList.stream().map(MeasureVo::convert).map(this::renderUser).collect(Collectors.toList());
        }
        long points = count(Wrappers.<Measure>lambdaQuery().eq(Measure::getSlideId, slideId)
                .eq(Measure::getLocationType, Geometry.TYPENAME_POINT));
        if (points > 0){
            measureVoList.add(MeasureVo.builder().pointCount(points).measureFullName("P").build());
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String exportName = URLEncoder.encode(MessageSource.M("EXCEL_TITLE"), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + exportName + ".xlsx");
        // 使用EasyExcel写入数据
        EasyExcel.write(response.getOutputStream(), MeasureVo.class).sheet(exportName).doWrite(measureVoList);
    }

    private MeasureVo renderUser(MeasureVo measureVo){
        if (measureVo == null){
            return null;
        }
        R<List<SysUser>> r = remoteUserService.query(new SysUser());
        List<SysUser> users = r == null ? null : r.getData();
        Map<Long, SysUser> userMap = users == null ? new HashMap<>() : users.stream()
                .filter(user -> user.getUserId() != null)
                .collect(Collectors.toMap(
                        SysUser::getUserId,
                        user -> user,
                        (existing, replacement) -> existing // 遇到重复 key 保留第一个
                ));


        Long createBy = measureVo.getCreateBy();
        if (createBy == null){
            return measureVo;
        }
        if (userMap == null){
            return measureVo;
        }
        String userName = userMap.get(createBy).getNickName();
        measureVo.setCreateUserName(userName);
        return measureVo;
    }
}




