package cn.staitech.anno.domain.files.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author: wangfeng
 * @create: 2023-09-15 17:14:35
 * @Description: 文件上传VO
 */
@Data
public class FileUploadVO implements Serializable {
    @ApiModelProperty(value = "业务类型(1原始切片，2预测图片，3切片信息表CSV，4待定)")
    private Integer businessType;
    @ApiModelProperty(value = "专题名称")
    private String topicName;
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 当前文件块，从0开始
     */
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    private Long chunkSize;

    /**
     * 切片总块数
     */
    private Integer totalChunks;

    /**
     * 专题id
     */
    private Long specialId;

    /**
     * 二进制文件(分片文件)
     */
    private MultipartFile multipartFile;

}
