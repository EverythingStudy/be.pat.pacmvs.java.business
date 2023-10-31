package cn.staitech.anno.service;

import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.organization.SysOrganizationAuthorization;
import cn.staitech.anno.domain.slide.SubImageVO;
import cn.staitech.anno.domain.specialsliceimage.SpecialSliceSelectVO;
import cn.staitech.anno.domain.specialsliceimage.SpecialSliceVo;
import cn.staitech.anno.utils.PageMaster;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 图像子表操作
 */
public interface SubImageService extends IService<SubImage> {

    R<PageMaster<SubImageVO>> pageSubImage(Map params);

    R<List<SubImageVO>> querySubImageByGroup(Map params);

    List<SpecialSliceVo> selectSpecialSliceVo(SpecialSliceSelectVO sisv);


    List<SubImage> selectSubImageList(SubImage subImage);

    void selectExpireSpecialSlice(SpecialSliceSelectVO sisv);

    public Map<String, String> getDictInfo(String dictType);


    void logOutSpecial(String userName);

    List<SubImage> selectImageCount(Long organizationId);

    SysOrganizationAuthorization selectOrganization(Long userId);

}
