package cn.staitech.anno.service.impl;

import cn.staitech.anno.mapper.SpecialReclaimMapper;
import cn.staitech.anno.mapper.SubImageMapper;
import cn.staitech.anno.service.SpecialReclaimService;
import cn.staitech.anno.vo.special.SpecialReclaim;
import cn.staitech.anno.vo.special.SpecialReclaimResVo;
import cn.staitech.anno.vo.special.SpecialReclaimSelectVo;
import cn.staitech.anno.vo.special.SpecialResVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/29 9:26
 */
@Service
public class SpecialReclaimServiceImpl implements SpecialReclaimService {

    @Resource
    private SpecialReclaimMapper specialReclaimMapper;

    @Resource
    private SubImageMapper subImageMapper;


    /**
     * 添加回收记录
     *
     * @param specialReclaim 回收信息
     * @return 0||1
     */
    @Override
    public int insert(SpecialReclaim specialReclaim) {
        return specialReclaimMapper.insert(specialReclaim);
    }


    /**
     * 查询出结果集，并进行合并
     *
     * @param specialList 专题信息
     * @return 0||1
     */
    @Override
    public List<SpecialReclaimResVo> selectList(List<SpecialResVo> specialList, SpecialReclaimSelectVo req) {
        List<SpecialReclaimResVo> specialReclaimResVos = new ArrayList<>();
        if (specialList.size() == 0) {
            return specialReclaimResVos;
        }
        for (SpecialResVo sp : specialList) {
            SpecialReclaim specialReclaim = new SpecialReclaim();
            specialReclaim.setSpecialId(sp.getSpecialId());
            specialReclaim.setReclaimTimeParams(req.getReclaimTimeParams());
            specialReclaim.setExpirationTimeParams(req.getExpirationTimeParams());
            // 根据id查询回收信息
            SpecialReclaim specialReclaimBy = specialReclaimMapper.selectOne(specialReclaim);
            // 将两表数据进行拼接
            SpecialReclaimResVo specialReclaimResVo = new SpecialReclaimResVo();
            // 判断专题id是否等于回收表中专题id
            if (specialReclaimBy != null) {
                if (sp.getSpecialId().equals(specialReclaim.getSpecialId())) {
                    BeanUtils.copyProperties(sp, specialReclaimResVo);
                    BeanUtils.copyProperties(specialReclaimBy, specialReclaimResVo);
                    specialReclaimResVo.setSpecialId(sp.getSpecialId());
                    specialReclaimResVo.setProjectNum(sp.getProjectNum());
                    specialReclaimResVo.setSlideNum(sp.getSlideNum());
                    specialReclaimResVo.setCreateBy(sp.getUserName());
                    specialReclaimResVos.add(specialReclaimResVo);
                }
            }
        }
        return specialReclaimResVos;
    }

}
