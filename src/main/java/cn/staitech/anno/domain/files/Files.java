package cn.staitech.anno.domain.files;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: wangfeng
 * @create: 2023-09-10 16:28:38
 * @Description:文件表
 * @TableName tb_files
 */
@TableName(value = "tb_files")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Files implements Serializable {

    /**
     * 文件ID
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "files_id")
    @ApiModelProperty(value = "")
    private Long filesId;

    /**
     * 文件名称（文件名）
     */
    @TableField(value = "files_name")
    @ApiModelProperty(value = "文件名称（文件名）")
    private String filesName;

    /**
     * 文件的绝对路径
     */
    @TableField(value = "files_path")
    @ApiModelProperty(value = "文件的绝对路径")
    private String filesPath;

    /**
     * 文件URL地址
     */
    @TableField(value = "files_url")
    @ApiModelProperty(value = "文件URL地址")
    private String filesUrl;

    /**
     * 文件大小
     */
    @TableField(value = "size")
    @ApiModelProperty(value = "文件大小")
    private Long size;

    /**
     * 文件格式
     */

    @TableField(value = "format")
    @ApiModelProperty(value = "文件格式")
    private String format;

    /**
     * 处理状态(0MD5校验不通过上传失败2上传成功)
     */

    @TableField(value = "process_flag")
    @ApiModelProperty(value = "处理状态(0MD5校验不通过上传失败2上传成功)")
    private Integer processFlag;

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
     * 逻辑删除状态（0删除，1未删除）
     */
    @TableField(value = "delete_flag")
    @ApiModelProperty(value = "")
    private Integer deleteFlag;

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
     * 机构名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    /**
     * 业务类型(1原始切片，2预测图片，3切片信息表CSV，4待定)
     */

    @TableField(value = "business_type")
    @ApiModelProperty(value = "业务类型(1原始切片，2预测图片，3切片信息表CSV，4待定)")
    private Integer businessType;

    /**
     * 图像来源(1前端上传，2目录选片，3TCP客户端上传)
     */
    @TableField(value = "source")
    @ApiModelProperty(value = "图像来源(1前端上传，2目录选片，3TCP客户端上传)")
    private Integer source;

    /**
     * 创建人id
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建人id")
    private Long createBy;

    /**
     * 更新人id
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value = "更新人id")
    private Long updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "", hidden = true)
    @TableField(exist = false)
    private Map<String, Object> params;

    @ApiModelProperty(value = "文件名称列表", hidden = true)
    private List<String> fileNameList;

}
