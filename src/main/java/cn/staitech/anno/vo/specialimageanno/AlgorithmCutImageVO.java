package cn.staitech.anno.vo.specialimageanno;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlgorithmCutImageVO {


    /**
     * imageId
     */
    @ApiModelProperty(value = "imageId")
    private Long imageId;


    /**
     * specialImageId
     */
    @ApiModelProperty(value = "specialImageId")
    private Long specialImageId;


    /**
     * 切片原文件跟目录
     */
    @ApiModelProperty(value = "filePath")
    private String filePath;


    @ApiModelProperty(value = "hostId")
    private Integer hostId;


    /**
     * 切片保存目录
     */
    @ApiModelProperty(value = "outPath")
    private String outPath;

    /**
     * 源切片名称
     */
    @ApiModelProperty(value = "imageName")
    private String imageName;


    /**
     * 标注结果列表
     */
    @ApiModelProperty(value = "标注结果列表")
    private List<SpecialAnnDataVO> annoData = new ArrayList<>();


}
