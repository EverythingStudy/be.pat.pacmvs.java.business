package cn.staitech.anno.domain.image.in;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author wangfeng
 */
@Data
public class ImageTopicVO {
    /**
     * 图像id
     */
    @ApiModelProperty(value = "切片编号-图像ID",required = true)
    private Long imageId;

    @Size(min = 1, max = 100, message = "切片编号长度不能超过100个字符")
    @ApiModelProperty(value = "文件名称-切片编号")
    private String imageName;

    @ApiModelProperty(value = "所属专题-ID")
    private Long topicId;
    @Size(min = 1, max = 100, message = "专题名称长度不能超过100个字符")
    @ApiModelProperty(value = "所属专题-专题名称",required = true)
    private String topicName;

    @ApiModelProperty(value = "修改人", required = false, hidden = true)
    private Long updateBy;

    @ApiModelProperty(value = "机构编号")
    private Long organizationId;

}
