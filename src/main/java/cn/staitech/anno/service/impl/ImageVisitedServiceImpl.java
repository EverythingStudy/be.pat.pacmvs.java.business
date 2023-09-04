package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.ImageVisited;
import cn.staitech.anno.mapper.ImageVisitedMapper;
import cn.staitech.anno.service.ImageVisitedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gjt.
 * @data 2023/5/25 16:16
 */
@Service
public class ImageVisitedServiceImpl implements ImageVisitedService {

    @Resource
    private ImageVisitedMapper imageVisitedMapper;

    /**
     * 查询最近访问下的图像信息
     *
     * @param recentlyVisitedId 机构id
     * @return list
     */
    @Override
    public List<ImageVisited> selectList(Long recentlyVisitedId){
        return imageVisitedMapper.selectList(recentlyVisitedId);
    }

}
