package cn.staitech.anno.domain.examine;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamineScoreBy {

    /**
     * 主键
     */
    @TableId(value = "examine_score_id", type = IdType.AUTO)
    @ApiModelProperty(value = "评分id")
    private Long examineScoreId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 项目题库id
     */
    @ApiModelProperty(value = "项目题库id")
    private Long questionProjectId;

    /**
     * 切片编号
     */
    @ApiModelProperty(value = "切片编号")
    private String imageName;

    /**
     * 答题者
     */
    @ApiModelProperty(value = "答题者")
    private String nickName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    /**
     * 应标个数
     */
    @ApiModelProperty(value = "应标个数")
    private Long shouldNumber;

    /**
     * 实标个数
     */
    @ApiModelProperty(value = "实标个数")
    private Long realityNumber;

    /**
     * 算法拟合区间
     */
    @ApiModelProperty(value = "miou拟合区间")
    private String miou;

    /**
     * 算法拟合区间
     */
    @ApiModelProperty(value = "fiou拟合区间")
    private String fiou;

    /**
     * 算法拟合区间
     */
    @ApiModelProperty(value = "biou拟合区间")
    private String biou;

    /**
     * 算法拟合区间
     */
    @ApiModelProperty(value = "tiou拟合区间")
    private String tiou;

    /**
     * 个人拟合度
     */
    @ApiModelProperty(value = "个人拟合度")
    private String personalFit;

    /**
     * 考试结果(1通过、2未通过)
     */
    @ApiModelProperty(value = "考试结果(1通过、2未通过)")
    private String examResults;

    /**
     * 操作状态（0：开始考试，考试完成）
     */
    @ApiModelProperty(value = "操作状态（0：开始考试，考试完成）")
    private String operateStatus;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    /**
     * 切片id
     */
    @ApiModelProperty(value = "切片id")
    private Long slideId;

    /**
     * json文件路径
     */
    @ApiModelProperty(value = "json文件路径")
    private String geojsonUrl;


    /**
     * 考生考试json文件路径
     */
    @ApiModelProperty(value = "考试json文件路径")
    private String examinationGeojsonUrl;


    /**
     * 交卷时间
     */
    @ApiModelProperty(value = "交卷时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String completeTime;

    /**
     * 交卷时间
     */
    @ApiModelProperty(value = "题库id")
    private Long questionId;

}
