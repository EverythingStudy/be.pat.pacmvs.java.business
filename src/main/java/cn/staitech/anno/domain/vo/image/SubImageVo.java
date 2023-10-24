package cn.staitech.anno.domain.vo.image;

import cn.staitech.anno.domain.Group;
import cn.staitech.anno.domain.SubImage;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mugw
 * @version 1.0
 * @description 图像子表vo
 * @date 2023/5/31 17:50:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubImageVo extends SubImage {

    /**
     * 图像id
     */
    @TableId(value = "image_id", type = IdType.AUTO)
    @ApiModelProperty(value = "", hidden = true)
    private Long imageId;

    private String projectNames;

    private String groupNames;

    private String projectIds;

    private String groupIds;

    //当前分组信息
    private Group group;

    private List<Long> imageIds;

}
