package cn.staitech.anno.vo.question.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author wudi
 * @Date 2023/9/26 11:25
 * @desc 分页查询考题列表输出
 */
@Data
public class GetQuestionListOut {

    @ApiModelProperty(value = "题库项目关联id")
    private Long questionProjectId;

    @ApiModelProperty(value = "考题id")
    private Long questionId;

    @ApiModelProperty(value = "应标个数")
    private Long shouldMarks;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "切片编号")
    private String imageCode;

    @ApiModelProperty(value = "图片名称")
    private String imageName;

    @ApiModelProperty(value = "缩略图url地址")
    private String thumbUrl;

    @ApiModelProperty(value = "专题号")
    private String topicName;

    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    @ApiModelProperty(value = "大小")
    private String size;

    @ApiModelProperty(value = "json文件名称")
    private String jsonName;

    @ApiModelProperty(value = "生成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "创建者")
    private String createName;
}
