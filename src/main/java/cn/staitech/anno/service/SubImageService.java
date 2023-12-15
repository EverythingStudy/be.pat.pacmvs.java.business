package cn.staitech.anno.service;

import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.vo.organization.SysOrganizationAuthorization;
import cn.staitech.anno.vo.slide.SubImageVO;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 图像子表操作
 */
public interface SubImageService extends IService<SubImage> {

    R<List<SubImageVO>> querySubImageByGroup(Map params);

    List<SubImage> selectSubImageList(SubImage subImage);

    Map<String, String> getDictInfo(String dictType);

    List<SubImage> selectImageCount(Long organizationId);

    SysOrganizationAuthorization selectOrganization(Long userId);
}
