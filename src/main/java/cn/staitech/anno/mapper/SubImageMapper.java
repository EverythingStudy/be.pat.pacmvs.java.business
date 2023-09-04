package cn.staitech.anno.mapper;

import java.util.List;
import java.util.Map;

import cn.staitech.anno.domain.organization.SysOrganizationAuthorization;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.vo.image.ImageRelVo;
import cn.staitech.anno.domain.vo.image.SubImageVo;
import org.apache.lucene.index.DocIDMerger;


/**
 * @author mugw
 * @version 1.0
 * @description 图像子表操作
 * @date 2023/5/29 16:24:35
 */
public interface SubImageMapper extends BaseMapper<SubImage> {

    List<ImageRelVo> selectImageRelByIds(@Param("imageIds") List<Long> imageIds);

    IPage<SubImageVo> pageSubImage(@Param("page") Page page,@Param("params") Map params);
    
    int updateByPrimaryKeySelective(SubImageVo record);

    /**
     * 根据专题id查询总数
     * @param specialId 专题id
     * @return int
     */
    int selectSpecialCounts(Long specialId);

    /**
     * 查询用户参与的专题下的图像数量
     */
    List<SubImage> selectImageCount(Long organizationId);

    SysOrganizationAuthorization selectOrganization(Long userId);

}
