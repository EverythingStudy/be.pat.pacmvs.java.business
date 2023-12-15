package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.SysDictData;
import cn.staitech.anno.mapper.SubImageMapper;
import cn.staitech.anno.service.SubImageService;
import cn.staitech.anno.service.SysDictDataService;
import cn.staitech.anno.vo.organization.SysOrganizationAuthorization;
import cn.staitech.anno.vo.slide.SubImageVO;
import cn.staitech.common.core.domain.R;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 图像操作
 * @date 2023/5/29 16:33:38
 */
@Slf4j
@Service
public class SubImageServiceImpl extends ServiceImpl<SubImageMapper, SubImage> implements SubImageService {

    @Resource
    private SubImageMapper subImageMapper;

    @Autowired
    private SysDictDataService sysDictDataService;

    /**
     * 根据分组查询切片
     *
     * @param params
     * @return
     */
    @Override
    public R<List<SubImageVO>> querySubImageByGroup(Map params) {
        Page<SubImageVO> page = new Page<>(1, Integer.MAX_VALUE);
        getBaseMapper().pageSubImage(page, params);
        return R.ok(page.getRecords());
    }

    @Override
    public List<SubImage> selectSubImageList(SubImage subImage) {
        Map paramMap = new HashMap<>(16);
        paramMap.put("special_id", subImage.getSpecialId());
        paramMap.put("parent_image_id", subImage.getParentImageId());
        paramMap.put("slice_batch_number", subImage.getSliceBatchNumber());
        List<SubImage> list = subImageMapper.selectByMap(paramMap);
        return list;
    }

    @Override
    public Map<String, String> getDictInfo(String dictType) {
        Map<String, String> organizationMap = new HashMap<String, String>(16);
        Map<String, Object> map = new HashMap<>(16);
        map.put("dictType", dictType);
        map.put("status", 0);
        List<SysDictData> dictDatas = sysDictDataService.getSysDictDataListByParm(map);
        if (CollectionUtils.isNotEmpty(dictDatas)) {
            for (SysDictData sysDate : dictDatas) {
                String dictLabel = sysDate.getDictLabel();
                String dictValue = sysDate.getDictValue();
                organizationMap.put(dictValue, dictLabel);
            }
        }
        return organizationMap;
    }

    @Override
    public List<SubImage> selectImageCount(Long organizationId) {
        return subImageMapper.selectImageCount(organizationId);
    }

    @Override
    public SysOrganizationAuthorization selectOrganization(Long userId) {
        return subImageMapper.selectOrganization(userId);
    }


}
