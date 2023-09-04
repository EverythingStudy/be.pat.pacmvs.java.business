package cn.staitech.anno.service;

import cn.staitech.anno.domain.po.VisceraTagPo;
import cn.staitech.anno.domain.vo.VisceraTagVo;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 脏器标签服务
 * @date 2023/6/26 16:26:42
 */
public interface VisceraTagService extends IService<VisceraTagPo> {

    R<PageMaster<VisceraTagVo>> pageVisceraTag(Map params);

}
