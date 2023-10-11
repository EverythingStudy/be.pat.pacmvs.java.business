package cn.staitech.anno.domain.image.in;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 选片查询VO
 * <p>
 * 标注类项目：
 * 查询条件：切片编号、专题号、状态、项目ID
 * 回显字段：缩略图、专题号、切片编号、图片大小、机构、上传时间、添加状态
 * <p>
 * 评审轮次切片列表-选片：
 * 查询条件：切片编号、专题号、状态、项目ID、roundReviewId
 * 回显字段：缩略图、专题号、切片编号、轮次、图片大小、机构、上传时间、添加状态
 *
 * @author wangfeng
 */
@Data
public class ImageTopicVO extends Pager implements Serializable {
    @Size(min = 0, max = 200, message = "切片编号长度不能超过100个字符")
    @ApiModelProperty(value = "文件名称-切片编号")
    private String imageName;
    @Size(min = 0, max = 200, message = "专题名称长度不能超过100个字符")
    @ApiModelProperty(value = "所属专题-专题名称")
    private String topicName;
    @ApiModelProperty(value = "添加状态：NULL查全部、0未添加、1已添加")
    private Integer choiceState;
    @ApiModelProperty(value = "项目编号")
    private Long projectId;
    @ApiModelProperty(value = "reviewRoundId")
    private Long reviewRoundId;
    @ApiModelProperty(value = "机构编号", hidden = true)
    private Long organizationId;
    @ApiModelProperty(value = "业务类型:1原始切片（默认）、2预测切片", hidden = true)
    private Integer bizType;
}
