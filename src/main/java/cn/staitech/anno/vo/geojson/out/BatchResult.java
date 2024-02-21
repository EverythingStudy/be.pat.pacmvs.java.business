package cn.staitech.anno.vo.geojson.out;

import lombok.Data;

/**
 * @author: wangfeng
 * @create: 2024-02-20 16:03:11
 * @Description: 批量操作返回值
 */
@Data
public class BatchResult {
    private String front_id;
    private String marking_id;
    private String message;
    private Boolean status;
}
