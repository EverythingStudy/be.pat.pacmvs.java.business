package cn.staitech.anno.service;

import cn.staitech.anno.domain.Organ;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 脏器
 */
public interface OrganService extends IService<Organ> {

    Map<String, String> selectMap();

    Map<String, String> selectMapEn();

    List<Organ> getOrganBySpeciesId(String speciesId);
}
