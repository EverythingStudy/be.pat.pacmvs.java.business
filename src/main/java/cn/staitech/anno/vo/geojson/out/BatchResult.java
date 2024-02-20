package cn.staitech.anno.vo.geojson.out;

import lombok.Data;

import java.util.List;

/**
 * @author: wangfeng
 * @create: 2024-02-20 16:03:11
 * @Description: 批量操作返回值
 */
@Data
public class BatchResult {
    private List<Message> success;
    private List<Message> fail;
}
