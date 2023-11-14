package cn.staitech.anno.vo.airepost;

import cn.staitech.anno.domain.Airepost;
import lombok.Data;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2023-11-10 17:52:04
 * @Description: 拼接Viewer列表查询条件
 */
@Data
public class AirepostList {

    private List<Airepost> airepostLists;
}

