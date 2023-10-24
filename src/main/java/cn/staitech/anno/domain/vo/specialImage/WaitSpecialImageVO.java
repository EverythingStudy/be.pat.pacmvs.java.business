package cn.staitech.anno.domain.vo.specialImage;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: WaitSpecialImageVO
 * @Description:选片vo
 * @date 2023年6月2日
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WaitSpecialImageVO {


    @ApiModelProperty(required = false, value = "imageId")
    private Long imageId;

    @ApiModelProperty(required = false, value = "缩略图")
    private String thumbUrl;
    @ApiModelProperty(required = false, value = "切片url")
    private String imageUrl;

    @ApiModelProperty(required = false, value = "切片编码")
    private String imageCode;

    @ApiModelProperty(required = false, value = "切片名称")
    private String imageName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(required = false, value = "所属专题")
    private Long belongSpecialId;

    @ApiModelProperty(required = false, value = "专题切片id")
    @Builder.Default
    private Long specialImageId = -1L;

}
