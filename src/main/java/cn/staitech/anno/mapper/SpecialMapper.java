package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.special.Special;
import cn.staitech.anno.domain.vo.special.SpecialResVo;
import cn.staitech.anno.domain.vo.special.SpecialSlideStatisticsVO;
import cn.staitech.anno.domain.vo.special.SpecialStatisticsListVO;
import cn.staitech.anno.domain.vo.special.SpecialStatisticsQueryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author gjt.
 * @data 2023/5/29 9:04
 */
public interface SpecialMapper extends BaseMapper<Special> {

    /**
     * 查询专题阅片下的专题列表
     *
     * @return Special
     */
    List<SpecialResVo> selectSpecialReadFilm(Special special);

    /**
     * 查询专题下的切片数量
     *
     * @return int
     */
    int selectSpecialSlideCount(Long specialId);

    /**
     * 查询专题回收中的专题信息列表
     *
     * @return Special
     */
    List<Special> selectSpecialList();

    /**
     * 根据主键查询详细信息
     *
     * @param specialId 专题id
     * @return special
     */
    SpecialResVo selectSpecialId(Long specialId);

    /**
     * 根据条件查询单条信息
     *
     * @param special 专题信息
     * @return special
     */
    Special selectSpecialNumber(Special special);

    /**
     * 根据主键查询详情
     *
     * @param specialId 专题id
     * @return Special
     */
    Special selectSpecialById(Long specialId);

    /**
     * 查询专题中所被使用的标签
     *
     * @param specialId 专题id
     * @return Special
     */
    List<Special> selectSpecialCategoryList(Long specialId);

    /**
     * 查询专题信息
     *
     * @param special 专题信息
     * @return List<Special>
     */
    List<SpecialResVo> selectList(Special special);

    /**
     * 专题统计
     *
     * @param special 专题信息
     * @return List<Special>
     */
    List<SpecialStatisticsListVO> specialStatistics(SpecialStatisticsQueryVO special);

    /**
     * 专题统计
     *
     * @param special 专题信息
     * @return List<Special>
     */
    List<SpecialStatisticsListVO> specialStatisticsAdmin(SpecialStatisticsQueryVO special);

    /**
     * 查询专题下的项目ID
     *
     * @param specialStatisticsListVO 用户id和专题ID
     * @return 项目ID
     */
    List<Long> queryProjectIdBySpecialId(SpecialStatisticsListVO specialStatisticsListVO);

    /**
     * admin查询专题下的项目ID
     *
     * @param specialId 专题ID
     * @return 项目ID
     */
    List<Long> queryProjectIdAdminBySpecialId(Long specialId);

    /**
     * 查询项目下的人工诊断状态
     *
     * @param projectId 项目ID
     * @return 人工诊断状态
     */
    List<SpecialSlideStatisticsVO> queryDiagnosisByProjectId(Long projectId);

    /**
     * 根据userid查询所属专题
     *
     * @param params
     * @return
     */
    List<Special> selectByUserId(@Param("params") Map params);

    /**
     * 添加专题信息
     *
     * @param special 专题
     * @return List
     */
    int insert(Special special);

    /**
     * 更新专题信息
     *
     * @param special 专题
     * @return true||false
     */
    int update(Special special);

    /**
     * 删除专题表中信息
     *
     * @param special 专题
     * @return true||false
     */
    int updateDelFlag(Special special);

    /**
     * 更新专题状态
     *
     * @param special 专题
     * @return true||false
     */
    int updateStatus(Special special);
}
