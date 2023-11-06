package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目图像表 tb_slide
 *
 * @author wangf
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel(description = "tb_slide")
@TableName(value = "tb_slide")
public class Slide {
    /**
     * 切片ID
     */
    @TableId(value = "slide_id", type = IdType.INPUT)
    @ApiModelProperty(value = "切片ID")
    private Long slideId;

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 图像ID
     */
    @TableField(value = "image_id")
    @ApiModelProperty(value = "图像ID")
    private Long imageId;

    /**
     * 人工标注数
     */
    @TableField(value = "human_annotation_total")
    @ApiModelProperty(value = "人工标注数")
    private Integer humanAnnotationTotal;

    /**
     * 算法标注数
     */
    @TableField(value = "algorithm_annotation_total")
    @ApiModelProperty(value = "算法标注数")
    private Integer algorithmAnnotationTotal;

    /**
     * 已审核切片数
     */
    @TableField(value = "examination_slide_total")
    @ApiModelProperty(value = "已审核切片数")
    private Integer examinationSlideTotal;

    /**
     * 处理状态（0未处理,1处理中,2处理完成）
     */
    @TableField(value = "process_flag")
    @ApiModelProperty(value = "处理状态（0未处理,1处理中,2处理完成）")
    private Integer processFlag;

    /**
     * 复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核不通过 4交付)
     */
    @TableField(value = "examination_flag")
    @ApiModelProperty(value = "复核状态 (0提交复核(未复核) 1开始复核(复核中) 2复核通过(已复核) 3复核不通过 4交付)")
    private Integer examinationFlag;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 切片描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "切片描述")
    private String description;

    /**
     * 分组id
     */
    @TableField(value = "group_id")
    @ApiModelProperty(value = "分组id")
    private Integer groupId;

    /**
     * 是否删除(0未删除 1已删除)
     */
    @TableField(value = "is_delete")
    @ApiModelProperty(value = "是否删除(0未删除 1已删除)")
    private Integer isDelete;

    /**
     * AI筛阴：1:阴性、2:阳性、0:未筛、3:未知
     */
    @TableField(value = "ai_check")
    @ApiModelProperty(value = "AI筛阴：1:阴性、2:阳性、0:未筛、3:未知")
    private Short aiCheck;

    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败
     */
    @TableField(value = "ai_analyzed")
    @ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败")
    private Short aiAnalyzed;

    /**
     * 人工诊断状态：0未诊断，1已诊断
     */
    @TableField(value = "diagnosis")
    @ApiModelProperty(value = "人工诊断状态：0未诊断，1已诊断")
    private Short diagnosis;

    /**
     * geojson文件地址
     */
    @TableField(value = "geojson_url")
    @ApiModelProperty(value = "geojson文件地址")
    private String geojsonUrl;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态(1未开始 2标注中 3标注完成 4提交复核(未复核) 5开始复核(复核中) 6复核通过(已复核) 7交付)
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "状态(1未开始 2标注中 3标注完成 4提交复核(未复核) 5开始复核(复核中) 6复核通过(已复核) 7交付)")
    private String status;

    /**
     * 轮次
     */
    @TableField(value = "round_id")
    @ApiModelProperty(value = "轮次")
    private Long roundId;

    /**
     * 评审轮次id
     */
    @TableField(value = "review_round_id")
    @ApiModelProperty(value = "评审轮次id")
    private Long reviewRoundId;

    /**
     * 专题id
     */
    @TableField(value = "topic_id")
    @ApiModelProperty(value = "专题id")
    private Long topicId;

    /**
     * csv文件记录ID
     */
    @TableField(value = "image_csv_id")
    @ApiModelProperty(value = "csv文件记录ID")
    private Long imageCsvId;

    /**
     * 组别
     */
    @TableField(value = "group_name")
    @ApiModelProperty(value = "组别")
    private String groupName;

    /**
     * 性别
     */
    @TableField(value = "gender")
    @ApiModelProperty(value = "性别")
    private String gender;

    /**
     * 种属
     */
    @TableField(value = "species")
    @ApiModelProperty(value = "种属")
    private String species;

    /**
     * 品系
     */
    @TableField(value = "product_series")
    @ApiModelProperty(value = "品系")
    private String productSeries;

    /**
     * 剂量
     */
    @TableField(value = "dosage")
    @ApiModelProperty(value = "剂量")
    private String dosage;

    /**
     * 实验动物来源
     */
    @TableField(value = "animal_source")
    @ApiModelProperty(value = "实验动物来源")
    private String animalSource;

    /**
     * 动物接收周龄
     */
    @TableField(value = "receiving_week")
    @ApiModelProperty(value = "动物接收周龄")
    private String receivingWeek;

    /**
     * 动物给药周期
     */
    @TableField(value = "dosing_cycle")
    @ApiModelProperty(value = "动物给药周期")
    private String dosingCycle;

    /**
     * 动物恢复周期
     */
    @TableField(value = "recovery_cycle")
    @ApiModelProperty(value = "动物恢复周期")
    private String recoveryCycle;

    /**
     * 死亡日期
     */
    @TableField(value = "date_of_death")
    @ApiModelProperty(value = "死亡日期")
    private String dateOfDeath;

    /**
     * 移走原因
     */
    @TableField(value = "remove_reason")
    @ApiModelProperty(value = "移走原因")
    private String removeReason;

    /**
     * 脏器
     */
    @TableField(value = "organ")
    @ApiModelProperty(value = "脏器")
    private String organ;

    /**
     * 病变类型1
     */
    @TableField(value = "lesion_type1")
    @ApiModelProperty(value = "病变类型1")
    private String lesionType1;

    /**
     * 病变程度1
     */
    @TableField(value = "lesion_degree1")
    @ApiModelProperty(value = "病变程度1")
    private String lesionDegree1;

    /**
     * 病变类型2
     */
    @TableField(value = "lesion_type2")
    @ApiModelProperty(value = "病变类型2")
    private String lesionType2;

    /**
     * 病变程度2
     */
    @TableField(value = "lesion_degree2")
    @ApiModelProperty(value = "病变程度2")
    private String lesionDegree2;

    /**
     * 专题名称
     */
    @TableField(value = "topic_name")
    @ApiModelProperty(value = "专题名称")
    private String topicName;

    /**
     * 逻辑删除状态（0删除,1未删除）
     */
    @TableField(value = "delete_flag")
    @ApiModelProperty(value = "逻辑删除状态（0删除,1未删除）")
    private Byte deleteFlag;

    /**
     * 机构ID
     */
    @TableField(value = "organization_id")
    @ApiModelProperty(value = "机构ID")
    private Long organizationId;

    /**
     * 所在主机ID
     */
    @TableField(value = "host_id")
    @ApiModelProperty(value = "所在主机ID")
    private Byte hostId;

    @TableField(value = "if_create_questions")
    @ApiModelProperty(value = "是否生成考题；0-未生成；1-已生成")
    private String ifCreateQuestions;

    @ApiModelProperty(value = "文件夹id")
    private Long folderId;

    @ApiModelProperty(value = "预测缩略图url")
    private Long predictionImageId;

    @ApiModelProperty(value = "碎片状态（默认为0校验通过，1校验不通过）")
    private String eyeMent;

    @ApiModelProperty(value = "提示语")
    private String prompt;

}
