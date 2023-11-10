package cn.staitech.anno.mapper;

import java.util.List;
import cn.staitech.anno.domain.Airepost;

/**
 * AirepostMapper接口
 * 
 * @author wangfeng
 * @date 2023-11-10
 */
public interface AirepostMapper 
{
    /**
     * 查询Airepost
     * 
     * @param reportUuid Airepost主键
     * @return Airepost
     */
    public Airepost selectAirepostByReportUuid(Long reportUuid);

    /**
     * 查询Airepost列表
     * 
     * @param airepost Airepost
     * @return Airepost集合
     */
    public List<Airepost> selectAirepostList(Airepost airepost);

    /**
     * 新增Airepost
     * 
     * @param airepost Airepost
     * @return 结果
     */
    public int insertAirepost(Airepost airepost);

    /**
     * 修改Airepost
     * 
     * @param airepost Airepost
     * @return 结果
     */
    public int updateAirepost(Airepost airepost);

    /**
     * 删除Airepost
     * 
     * @param reportUuid Airepost主键
     * @return 结果
     */
    public int deleteAirepostByReportUuid(Long reportUuid);

    /**
     * 批量删除Airepost
     * 
     * @param reportUuids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAirepostByReportUuids(Long[] reportUuids);
}
