package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.special.SpecialReclaim;

/**
 * @author gjt.
 * @data 2023/5/29 9:04
 */
public interface SpecialReclaimMapper {

    /**
     * 添加回收记录
     *
     * @param specialReclaim 回收信息
     * @return 0||1
     */
    int insert(SpecialReclaim specialReclaim);

    /**
     * 根据专题id查询列表,按照时间排序,并取出第一条数据
     *
     * @param specialReclaim 专题信息
     * @return 0||1
     */
    SpecialReclaim selectOne(SpecialReclaim specialReclaim);

    /**
     * 根据专题id查询列表,按照时间排序,并取出第一条数据
     *
     * @param specialId 专题id
     * @return SpecialReclaim 专题回收信息
     */
    SpecialReclaim selectById(Long specialId);


}
