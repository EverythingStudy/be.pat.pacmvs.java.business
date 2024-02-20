package cn.staitech.anno.vo.geojson.in;

import lombok.Data;

import java.util.List;

/**
 * 轮廓合并批量操作
 *
 * @author wangfeng
 */
@Data
public class MarkingUpdateInList {
    private List<MarkingUpdateIn> list;
}
