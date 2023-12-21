package cn.staitech.anno.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.staitech.anno.domain.Organ;
import cn.staitech.anno.vo.organ.InsertOrganVO;
import cn.staitech.common.core.domain.R;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 脏器
 */
public interface OrganService extends IService<Organ> {

    Map<String, String> selectMap();

    Map<String, String> selectMapEn();

    List<Organ> getOrganBySpeciesId(String speciesId);
    
    R<Organ> add(InsertOrganVO req);
}
