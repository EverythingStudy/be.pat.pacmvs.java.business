package cn.staitech.anno.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.staitech.anno.mapper.AirepostMapper;
import cn.staitech.anno.domain.Airepost;
import cn.staitech.anno.service.AirepostService;

/**
 * AirepostService业务层处理
 * 
 * @author wangfeng
 * @date 2023-11-10
 */
@Service
public class AirepostServiceImpl implements AirepostService
{
    @Autowired
    private AirepostMapper airepostMapper;

    /**
     * 查询Airepost
     * 
     * @param reportUuid Airepost主键
     * @return Airepost
     */
    @Override
    public Airepost selectAirepostByReportUuid(Long reportUuid)
    {
        return airepostMapper.selectAirepostByReportUuid(reportUuid);
    }

    /**
     * 查询Airepost列表
     * 
     * @param airepost Airepost
     * @return Airepost
     */
    @Override
    public List<Airepost> selectAirepostList(Airepost airepost)
    {
        List<Airepost> list = airepostMapper.selectAirepostList(airepost);
        for (Airepost obj : list) {
            obj.setJsonAddr(obj.getJsonAddr());
        }
        return list;
    }

    /**
     * 新增Airepost
     * 
     * @param airepost Airepost
     * @return 结果
     */
    @Override
    public int insertAirepost(Airepost airepost)
    {
        return airepostMapper.insertAirepost(airepost);
    }

    /**
     * 修改Airepost
     * 
     * @param airepost Airepost
     * @return 结果
     */
    @Override
    public int updateAirepost(Airepost airepost)
    {
        return airepostMapper.updateAirepost(airepost);
    }

    /**
     * 批量删除Airepost
     * 
     * @param reportUuids 需要删除的Airepost主键
     * @return 结果
     */
    @Override
    public int deleteAirepostByReportUuids(Long[] reportUuids)
    {
        return airepostMapper.deleteAirepostByReportUuids(reportUuids);
    }

    /**
     * 删除Airepost信息
     * 
     * @param reportUuid Airepost主键
     * @return 结果
     */
    @Override
    public int deleteAirepostByReportUuid(Long reportUuid)
    {
        return airepostMapper.deleteAirepostByReportUuid(reportUuid);
    }
}
