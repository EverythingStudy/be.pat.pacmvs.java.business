package cn.staitech.anno.project.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author mugw
 * @version 1.0
 * @description
 * @date 2023/9/14 10:49:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideVO {

    /**
     * 切片ID
     */
    private Long slideId;
    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @Size(max= 200,message="{PathologicalIndicatorCategory.categoryName.length}")
    @ApiModelProperty("缩略图URL地址")
    @Length(max= 200,message="{PathologicalIndicatorCategory.categoryName.length}")
    private String thumbUrl;

    @Size(max= 255,message="{projectType.length}")
    @ApiModelProperty("图片（切片）编号")
    @Length(max= 255,message="{projectType.length}")
    private String imageCode;

    /**
     * 组别
     */
    @Size(max= 255,message="{projectType.length}")
    @ApiModelProperty("组别")
    @Length(max= 255,message="{projectType.length}")
    private String groupName;
    /**
     * 性别
     */
    @Size(max= 255,message="{projectType.length}")
    @ApiModelProperty("性别")
    @Length(max= 255,message="{projectType.length}")
    private String gender;
    /**
     * 种属
     */
    @Size(max= 255,message="{projectType.length}")
    @ApiModelProperty("种属")
    @Length(max= 255,message="{projectType.length}")
    private String species;
    /**
     * 品系
     */
    @Size(max= 255,message="{projectType.length}")
    @ApiModelProperty("品系")
    @Length(max= 255,message="{projectType.length}")
    private String productSeries;

    @ApiModelProperty("人工标注数")
    private String manualAnnoDetails;

    @ApiModelProperty("人工标注总数")
    private Integer manualAnnoCount;

    @ApiModelProperty("标注类别")
    private String categoryTypes;
    /**
     * 备注
     */
    @Size(max= 4096,message="{Slide.remark.isnull}")
    @ApiModelProperty("备注")
    @Length(max= 4096,message="{Slide.remark.isnull}")
    private String remark;
    /**
     * 状态
     */
    @NotBlank(message="{Slide.status.isnull}")
    @Size(max= 1,message="{Slide.status.length}")
    @ApiModelProperty("状态(0未开始 1标注中 2标注完成 3提交复核(未复核) 4开始复核(复核中) 5复核通过(已复核) 6交付)")
    @Length(max= 1,message="{Slide.status.length}")
    private String status;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否生成考题；0-未生成；1-已生成")
    private String ifCreateQuestions;

}
