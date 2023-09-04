package cn.staitech.anno.mapper;

import cn.staitech.anno.domain.po.VisceraTagPo;
import cn.staitech.anno.domain.vo.VisceraTagVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 切片 数据层
 *
 * @author staitech
 */
public interface VisceraTagMapper extends BaseMapper<VisceraTagPo> {

    /**
     * 分页查询脏器标签
     * @param params
     * @return
     */
    IPage<VisceraTagVo> pageVisceraTag(@Param("page") Page page, @Param("params") Map params);
}