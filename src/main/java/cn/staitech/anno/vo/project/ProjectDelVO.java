package cn.staitech.anno.vo.project;

import cn.staitech.anno.vo.eyeslide.EyeProjectSlideOut;
import cn.staitech.anno.vo.image.out.ImageListOutVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDelVO {

    @ApiModelProperty(required = true, value = "项目id")
    private Long projectId;

    /**
     * 通过项目id查询对应的图像列表
     */
    @ApiModelProperty(value = "", hidden = true)
    private Integer pageNum;

    @ApiModelProperty(value = "", hidden = true)
    private Integer pageSize;

    @ApiModelProperty(value = "", hidden = true)
    private Boolean flag;

    @ApiModelProperty(value = "", hidden = true)
    private List<EyeProjectSlideOut> result;

    @ApiModelProperty(value = "", hidden = true)
    private List<ImageListOutVO> results;
}
