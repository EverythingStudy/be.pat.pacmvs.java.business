package cn.staitech.anno.vo.files;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: wangfeng
 * @create: 2023-09-15 17:14:35
 * @Description: 文件上传VO
 */
@Data
public class FileUploadNoVO implements Serializable {
    @NotNull(message = "{FileUploadVO.businessType.notNull}")
    @ApiModelProperty(value = "业务类型(1原始切片，2预测图片，3切片信息表CSV，4:json压缩包(zip)")
    private Integer businessType;

    @ApiModelProperty(value = "专题名称")
    private String topicName;

    @ApiModelProperty(value = "机构ID")
    private Long organizationId;

    @NotBlank(message = "{FileUploadVO.fileName.notBlank}")
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /**
     * 当前文件块，从0开始
     */
    private Integer chunk;
    /**
     * 分块大小
     */
    private Long chunkSize;

    /**
     * 切片总块数
     */
    private Integer chunkTotal;

    /**
     * 专题id
     */
    private Long specialId;
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "文件夹路径")
    private String fileUrl;

    @ApiModelProperty(value = "评审轮次")
    private Long roundId;

    @ApiModelProperty(value = "项目类型")
    private Long projectTypeId;

    @ApiModelProperty(value = "结构编码")
    private String number;

    /**
     * 分块大小
     */
    private Long filesId;
    @ApiModelProperty(value = "uuid")
    private String uuid;

}
