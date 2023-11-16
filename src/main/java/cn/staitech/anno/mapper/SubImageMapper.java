package cn.staitech.anno.mapper;

import cn.staitech.anno.vo.image.ImageRelVO;
import cn.staitech.anno.vo.organization.SysOrganizationAuthorization;
import cn.staitech.anno.vo.slide.SubImageVO;
import cn.staitech.anno.domain.SubImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @author mugw
 * @version 1.0
 * @description 图像子表操作
 * @date 2023/5/29 16:24:35
 */
public interface SubImageMapper extends BaseMapper<SubImage> {

    List<ImageRelVO> selectImageRelByIds(@Param("imageIds") List<Long> imageIds);

    IPage<SubImageVO> pageSubImage(@Param("page") Page page, @Param("params") Map params);

    int updateByPrimaryKeySelective(SubImageVO record);

    /**
     * 根据专题id查询总数
     *
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
