package cn.staitech.anno.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * aipre_slide_prediction
 * @author 
 */
@Data
public class AipreSlidePrediction implements Serializable {
    /**
     * 切片预测ID
     */
    private Long slidePredictionId;

    /**
     * 项目ID
     */
    private Integer slideId;

    /**
     * 图像ID
     */
    private Integer imageId;

    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败）
     */
    private Short aiAnalyzed;

    /**
     * 描述
     */
    private String description;

    /**
     * 机构ID
     */
    private Long organizationId;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    private static final long serialVersionUID = 1L;
}