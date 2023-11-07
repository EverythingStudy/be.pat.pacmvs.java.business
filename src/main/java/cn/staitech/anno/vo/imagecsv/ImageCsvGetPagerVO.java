package cn.staitech.anno.vo.imagecsv;

import cn.staitech.anno.domain.Pager;
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
public class ImageCsvGetPagerVO extends Pager implements Serializable {

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Long projectId;
    /**
     * 切片编号
     */
    @ApiModelProperty(value = "切片编号")
    private String imageName;

    /**
     * 组别
     */
    @ApiModelProperty(value = "组别")
    private String groupName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;

    /**
     * 病变类型1
     */
    @ApiModelProperty(value = "病变类型1")
    private String lesionType;

    /**
     * 病变程度1
     */
    @ApiModelProperty(value = "病变程度1")
    private String lesionDegree;
    
    /**
     * 文件名称
     */
    private String folderName;
    
    
    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败
     */
    private Integer aiAnalyzed;
    

    /**
     * reviewRoundId
     */
    @ApiModelProperty(value = "reviewRoundId")
    private Long reviewRoundId;

    @ApiModelProperty("请求参数（开始和结束时间）")
    private Map<String, Object> createTimeParams;

    @ApiModelProperty(value = "碎片状态(默认为0校验通过，1校验不通过)",hidden = true)
    private String eyeMent;
}
