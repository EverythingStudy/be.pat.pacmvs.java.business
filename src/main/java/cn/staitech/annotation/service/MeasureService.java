package cn.staitech.annotation.service;

import cn.staitech.annotation.domain.Measure;
import cn.staitech.common.core.domain.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author mugw
 * @version 1.0
 * @description 测量标注管理
 * @date 2025/5/21 14:33:43
 */
public interface MeasureService extends IService<Measure> {

    R<Measure> addMeasure(Measure req) throws Exception;

    R delete(Long measureId) throws Exception ;

    void export(Long slideId) throws Exception;

}
