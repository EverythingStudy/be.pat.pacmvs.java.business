package cn.staitech.anno.domain.vo.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mugw
 * @version 1.0
 * @description 切片关联属性
 * @date 2023/6/1 09:49:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRelVo {

    private Long imageId;

    private String projectNames;

    private String groupNames;

    private String projectIds;

    private String groupIds;

}
