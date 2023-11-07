package cn.staitech.anno.vo.imagecsv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: wangfeng
 * @create: 2023-09-20 17:46:52
 * @Description: Image CSV查询条件
 * 切片编号 组别 性别 病变程度 病变类型
 */
@Getter
@Setter
@Data
public class ImageCsvGetVO implements Serializable {

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 切片编号
     */
    private String imageName;

    /**
     * 组别
     */
    private String groupName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 病变类型1
     */
    private String lesionType;

    /**
     * 病变程度1
     */
    private String lesionDegree;
    
    /**
     * 文件名称
     */
    private String folderName;
    
    
    /**
     * 碎片状态默认为0校验通过，1校验不通过
     */
    private String eyeMent;
    
    
    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败
     */
    private Integer aiAnalyzed;

    /**
     * reviewRoundId
     */
    private Long reviewRoundId;

    @ApiModelProperty("请求参数（开始和结束时间）")
    private Map<String, Object> createTimeParams;
}
