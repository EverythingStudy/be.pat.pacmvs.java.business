package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.po.VisceraTagPo;
import cn.staitech.anno.domain.vo.VisceraTagVo;
import cn.staitech.anno.domain.vo.image.SubImageVo;
import cn.staitech.anno.mapper.VisceraTagMapper;
import cn.staitech.anno.service.VisceraTagService;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/6/26 16:29:16
 */
@Service
public class VisceraTagServiceImpl extends ServiceImpl<VisceraTagMapper, VisceraTagPo> implements VisceraTagService {

    @Override
    public R<PageMaster<VisceraTagVo>> pageVisceraTag(Map params) {
        Page<SubImageVo> page = new Page<>(MapUtils.getInteger(params,"pageNum",1),MapUtils.getInteger(params,"pageSize",10));
        getBaseMapper().pageVisceraTag(page,params);
        //构建分页对象
        PageMaster pageMaster = PageMaster.of(page.getRecords());
        pageMaster.setTotal(page.getTotal());
        return R.ok(pageMaster);
    }
}
