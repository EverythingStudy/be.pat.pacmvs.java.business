package cn.staitech.anno.domain.files.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: wangfeng
 * @create: 2023-09-15 17:14:35
 * @Description: 文件上传VO
 */
@Data
@Builder
public class FileUploadVO {
    @ApiModelProperty(value = "上传的文件MultipartFile")
    private MultipartFile file;
    @ApiModelProperty(value = "业务类型(1原始切片，2预测图片，3切片信息表CSV，4待定)")
    private Integer businessType;
    @ApiModelProperty(value = "专题号")
    private Long topicId;
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;
}
