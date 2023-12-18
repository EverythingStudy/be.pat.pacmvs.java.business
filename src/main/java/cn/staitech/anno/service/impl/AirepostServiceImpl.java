package cn.staitech.anno.service.impl;

import cn.staitech.anno.domain.Airepost;
import cn.staitech.anno.mapper.AirepostMapper;
import cn.staitech.anno.service.AirepostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 业务层处理：AI预测-算法项目-眼科拼接Viewer-View图像
 *
 * @author wangfeng
 * @date 2023-11-10
 */
@Slf4j
@Service
public class AirepostServiceImpl extends ServiceImpl<AirepostMapper, Airepost> implements AirepostService {
    @Resource
    private AirepostMapper airepostMapper;

    /**
     * 查询Airepost列表
     *
     * @param airepost Airepost
     * @return Airepost
     */
    @Override
    public List<Airepost> selectAirepostList(Airepost airepost) {
        List<Airepost> list = airepostMapper.selectAirepostList(airepost);
        for (Airepost obj : list) {
            String jsonAddr = obj.getJsonAddr();
            // 处理图像URL:{/home/pat_saas}/C001/Slides/image/info/cropped/tgt/1.png => {/file/statics}/C001/Slides/thumbnail/20230928/75/0.jpg
            obj.setAiImageUrl(jsonAddr.replace("/home/pat_saas", "/file/statics"));
            obj.setFileName(jsonAddr.substring(jsonAddr.lastIndexOf(File.separator) + 1));
        }
        return list;
    }

    /**
     * 重置
     *
     * @param airepost Airepost
     * @return Airepost
     */
    @Override
    public boolean reset(Airepost airepost) {
        List<Airepost> list = airepostMapper.selectAirepostList(airepost);
        for (Airepost obj : list) {
            obj.setCenterX(obj.getInitCenterX());
            obj.setCenterY(obj.getInitCenterY());
            obj.setLevel(obj.getInitLevel());
            obj.setRotation(0);
            obj.setVisible(true);
        }
        log.info("Airepost list:{}", list);
        return updateBatchById(list);
    }
}
