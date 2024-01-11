package cn.staitech.anno.vo.predictioninfo.in;

import cn.staitech.anno.domain.Pager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SlideImagePagerVO
 * @Description:
 * @date 2023年11月9日
 */
@Getter
@Setter
@Data
public class SlideImagePagerVO extends Pager implements Serializable {
    /**
     * @Fields serialVersionUID :
     */
    private static final long serialVersionUID = 1L;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    @NotNull(message = "{SlideImagePagerVO.projectId.isnull}")
    private Long projectId;
    /**
     * 切片编号
     */
    @ApiModelProperty(value = "切片编号")
    private String imageName;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String folderName;


    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败
     */
    @ApiModelProperty(value = "AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败")
    private Integer aiAnalyzed;
}

