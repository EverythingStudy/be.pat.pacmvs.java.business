package cn.staitech.anno.domain.files.in;

import cn.staitech.anno.domain.Pager;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author: wangfeng
 * @create: 2023-09-10 17:52:35
 * @Description: 查询Files
 * 切片信息列表-上传信息表-查询 ：切片编号、专题号、机构ID、上传时间-开始时间、上传时间-结束时间
 */
@Data
public class FilesListVO extends Pager implements Serializable {
    /**
     * 文件名称（文件名）
     */
    @TableField(value = "files_name")
    @ApiModelProperty(value = "文件名称")
    private String filesName;

    /**
     * 图片（切片）编号
     */

    @TableField(value = "files_code")
    @ApiModelProperty(value = "图片（切片）编号")
    private String filesCode;

    /**
     * 专题ID
     */
    @TableField(value = "topic_id")
    @ApiModelProperty(value = "专题ID")
    private Long topicId;

    /**
     * 专题名称
     */
    @TableField(value = "topic_name")
    @ApiModelProperty(value = "专题名称")
    private String topicName;

    /**
     * 是否可用0不可用1可用
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "是否可用0不可用1可用")
    private Integer status;

    /**
     * 所在主机ID
     */
    @TableField(value = "host_id")
    @ApiModelProperty(value = "所在主机ID")
    private Integer hostId;

    /**
     * 机构ID
     */
    @TableField(value = "organization_id")
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;

    /**
     * 业务类型(1原始切片，2预测图片，3切片信息表CSV，4待定)
     */
    @TableField(value = "business_type")
    @ApiModelProperty(value = "业务类型(1原始切片，2预测图片，3切片信息表CSV，4待定)")
    private Integer businessType;

}
