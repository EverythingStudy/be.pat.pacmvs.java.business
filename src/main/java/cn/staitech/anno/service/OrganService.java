package cn.staitech.anno.service;

import cn.staitech.anno.domain.organ.Organ;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author: wangfeng
 * @create: 2023-09-10 13:10:18
 * @Description: 脏器
 */
@Service
public interface OrganService extends IService<Organ> {

    Map<String, String> selectMap();
}
