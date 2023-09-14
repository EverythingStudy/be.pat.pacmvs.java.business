package cn.staitech.anno.service;

import cn.staitech.anno.domain.structure.Structure;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 结构
 */
public interface StructureService extends IService<Structure> {

    Map<String, String> selectMap();
}
