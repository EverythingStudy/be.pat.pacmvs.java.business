package cn.staitech.anno.service;

import cn.staitech.anno.domain.special.SpecialReclaim;
import cn.staitech.anno.domain.vo.special.SpecialReclaimResVo;
import cn.staitech.anno.domain.vo.special.SpecialReclaimSelectVo;
import cn.staitech.anno.domain.vo.special.SpecialResVo;

import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/29 9:25
 */
public interface SpecialReclaimService {

    /**
     * 添加回收记录
     *
     * @param specialReclaim 回收信息
     * @return 0||1
     */
    int insert(SpecialReclaim specialReclaim);


    /**
     * 查询出结果集，并进行合并
     *
     * @param specialList 专题信息
     * @return 0||1
     */
    List<SpecialReclaimResVo> selectList(List<SpecialResVo> specialList, SpecialReclaimSelectVo req);
}
