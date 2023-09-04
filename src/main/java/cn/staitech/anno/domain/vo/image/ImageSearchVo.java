package cn.staitech.anno.domain.vo.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 切片查询参数对象
 * @date 2023/6/1 14:33:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageSearchVo {

    private String imageCode;
    private String parentImageCode;
    private String visceraType;
    private String groupId;
    private Map accessTime;
}
