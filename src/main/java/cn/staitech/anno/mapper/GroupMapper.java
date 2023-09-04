package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.vo.GroupListVO;
import cn.staitech.anno.domain.vo.GroupVO;
import cn.staitech.anno.domain.vo.ProjectGroupAddVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
 * 分组 数据层
 *
 * @author zmj
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 添加分组
     * */
    public int insertSelective(Group record);

    /**
     * 查询分组数量
     * */
    public int selectGroup(Group group);

    /**
     * 获取所有分组
     * */
    public List<GroupListVO> selectAllGroup(Group group);

    /**
     * 更新分组信息
     * */
    public Long updateGroup(GroupVO group);

    /**
     * 项目分组表添加数据
     * */
    public int insertBatch(List<ProjectGroupAddVO> groupAddVOS);

    /**
     * 获取项目id
     * */
    public List<ProjectGroupAddVO> selectProject(Long specialId);

    /**
     * 查询单条分组信息
     * */
    public GroupListVO selectOneGroup(Group group);

    /**
     * 查询分组关联切片的，分组
     * */
    public int selectSlide(Long groupId);

    /**
     * 查询分组关联的项目
     * */
    public List<ProjectGroupAddVO> selectProjectGroup(ProjectGroupAddVO projectGroupAddVO);


}