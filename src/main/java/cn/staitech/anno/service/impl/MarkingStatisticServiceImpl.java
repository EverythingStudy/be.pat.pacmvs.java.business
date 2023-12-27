package cn.staitech.anno.service.impl;

import cn.staitech.anno.constant.CommonConstant;
import cn.staitech.anno.domain.MarkingStatistic;
import cn.staitech.anno.mapper.MarkingMapper;
import cn.staitech.anno.service.MarkingStatisticService;
import cn.staitech.anno.utils.Column;
import cn.staitech.anno.utils.ExcelTool;
import cn.staitech.anno.utils.ExcelUtil;
import cn.staitech.anno.utils.MessageSource;
import cn.staitech.anno.vo.marking.Marking;
import cn.staitech.anno.vo.marking.MarkingStatisticSelectVO;
import cn.staitech.common.security.utils.SecurityUtils;
import cn.staitech.system.api.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 标注 - 标签统计
 *
 * @author wangfeng
 */
@Service
@Slf4j
public class MarkingStatisticServiceImpl implements MarkingStatisticService {
    @Resource
    private MarkingMapper markingMapper;

    /**
     * 标签统计
     *
     * @param selectVO
     * @return
     */
    @Override
    public List<MarkingStatistic> selectMarkingStatistic(MarkingStatisticSelectVO selectVO) {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long organizationId = sysUser.getOrganizationId();

        selectVO.setOrganizationId(organizationId);
        selectVO.setUserId(sysUser.getUserId());
        List<MarkingStatistic> list = markingMapper.selectMarkingStatistic(selectVO);
        list = setCount(list, organizationId);

        return list;
    }


    /**
     * 标签统计 - 导出execl
     *
     * @param selectVO
     * @param response
     * @throws Exception
     */
    @Override
    public void execlExport(MarkingStatisticSelectVO selectVO, HttpServletResponse response) throws Exception {
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        Long organizationId = sysUser.getOrganizationId();

        selectVO.setOrganizationId(organizationId);
        selectVO.setUserId(sysUser.getUserId());
        List<MarkingStatistic> list = markingMapper.selectMarkingStatistic(selectVO);
        list = setCount(list, organizationId);

        // 构造表头的每个列头 定义表头
        List<Map<String, String>> titleList = ExcelUtil.getTitleList(CommonConstant.MARKING_STATISTICS_KEY, CommonConstant.MARKING_STATISTICS_VALUE);
        // 生成excel文件
        ExcelTool excelTool = new ExcelTool(MessageSource.M("MARKING_STATISTICS_EXCEL_TITLE"), 20, 20);
        List<Column> titleData = excelTool.columnTransformer(titleList);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        excelTool.exportExcel(titleData, list, response.getOutputStream(), true, false);
    }

    /**
     * 更新记录数
     *
     * @param list
     * @param organizationId
     * @return
     */
    public List<MarkingStatistic> setCount(List<MarkingStatistic> list, Long organizationId) {
        for (MarkingStatistic markingStatistic : list) {
            Marking marking = new Marking();
            marking.setCategory_id(Long.valueOf(markingStatistic.getCategoryId()));
            marking.setCreate_by(markingStatistic.getUserId());
            marking.setProject_id(markingStatistic.getProjectId());
            marking.setOrganization_id(organizationId);
            markingStatistic.setMarkingNum(markingMapper.selectMarkingNum(marking));
        }
        return list;
    }
}
