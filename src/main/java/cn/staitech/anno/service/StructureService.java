package cn.staitech.anno.service;

import cn.staitech.anno.domain.structure.Structure;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 结构
 */
@Service
public interface StructureService extends IService<Structure> {

    Map<String, String> selectMap();
}
