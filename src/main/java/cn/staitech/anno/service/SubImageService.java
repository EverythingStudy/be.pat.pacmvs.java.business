package cn.staitech.anno.service;

import cn.staitech.anno.utils.PageMaster;
import cn.staitech.anno.vo.organization.SysOrganizationAuthorization;
import cn.staitech.anno.vo.slide.SubImageVO;
import cn.staitech.anno.vo.specialsliceimage.SpecialSliceSelectVO;
import cn.staitech.anno.vo.specialsliceimage.SpecialSliceVo;
import cn.staitech.anno.domain.SubImage;
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
