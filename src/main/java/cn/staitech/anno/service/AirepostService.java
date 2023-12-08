package cn.staitech.anno.service;

import cn.staitech.anno.domain.Airepost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * AirepostService接口
 *
 * @author wangfeng
 * @date 2023-11-10
 */
public interface AirepostService extends IService<Airepost> {
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
     * 批量删除Airepost
     *
     * @param reportUuids 需要删除的Airepost主键集合
     * @return 结果
     */
    public int deleteAirepostByReportUuids(Long[] reportUuids);

    /**
     * 删除Airepost信息
     *
     * @param reportUuid Airepost主键
     * @return 结果
     */
    public int deleteAirepostByReportUuid(Long reportUuid);
}
