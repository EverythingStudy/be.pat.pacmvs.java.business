package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.FilesMapper;
import cn.staitech.anno.service.FilesService;
import cn.staitech.anno.service.SysOrganizationService;
import cn.staitech.anno.service.TopicService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.files.Files;
import cn.staitech.anno.vo.files.in.FilesListVO;
import cn.staitech.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author wangf
 * @description 针对表【tb_files】的数据库操作Service实现
 * @createDate 2023-09-10 17:04:40
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
        implements FilesService {
    @Resource
    private FilesMapper filesMapper;
    @Resource
    private SysOrganizationService organizationService;

    @Resource
    private TopicService topicService;

    @Override
    public PageMaster<Files> selectList(FilesListVO req) throws ExecutionException, InterruptedException {
        // 分页
        PageHelper.startPage(req.getPageNum(), req.getPageSize()).setReasonable(true);

        Files files = new Files();
        BeanUtils.copyProperties(req, files);

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();

        if (req.getTopicName() != null && req.getTopicName() != "" && req.getTopicName() != "null") {
            queryWrapper.like("topic_name", req.getTopicName());
        }
        if (req.getFilesName() != null && !"".equals(req.getFilesName()) && !"null".equals(req.getFilesName())) {
            queryWrapper.like("files_name", req.getFilesName());
        }
        if (req.getCreateTimeParams() != null && req.getCreateTimeParams().containsKey("beginTime")) {
            queryWrapper.ge("create_time", req.getCreateTimeParams().get("beginTime"));
        }
        if (req.getCreateTimeParams() != null && req.getCreateTimeParams().containsKey("endTime")) {
            queryWrapper.le("create_time", req.getCreateTimeParams().get("endTime"));
        }
        if(!SecurityUtils.isAdmin(SecurityUtils.getLoginUser().getSysUser().getUserId())){
            queryWrapper.eq("organization_id",SecurityUtils.getLoginUser().getSysUser().getOrganizationId());
        }

        queryWrapper.orderByDesc("files_id");

        //  查询图像列表
        List<Files> list = filesMapper.selectList(queryWrapper);
        // 机构列表
        Map<Long, String> organizationMap = organizationService.selectMap();
        // 专题列表
        Map<Long, String> topicMap = topicService.selectMap(1);

        for (Files obj : list) {
            // 机构名称
            if (organizationMap.containsKey(obj.getOrganizationId())) {
                obj.setOrganizationName(organizationMap.get(obj.getOrganizationId()));
            }
            // 专题名称
            if (topicMap.containsKey(obj.getTopicId())) {
                obj.setTopicName(topicMap.get(obj.getTopicId()));
            }
        }

        PageMaster pageMaster = new PageMaster<>(list);
        pageMaster.setList(list);
        return pageMaster;
    }
}




