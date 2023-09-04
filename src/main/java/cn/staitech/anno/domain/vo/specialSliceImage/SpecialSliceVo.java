package cn.staitech.anno.domain.vo.specialSliceImage;

import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;

import cn.staitech.anno.domain.SubImage;
import cn.staitech.anno.domain.specilaImage.SpecialImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
* @ClassName: SliceImageVO
* @Description:切片配置列表
* @author wanglibei
* @date 2023年6月6日
* @version V1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialSliceVo extends SpecialImage {
	List<SubImage> subImageList;
	
	@ApiModelProperty(value = "是否可编辑 1:不可以编辑 2：可以编辑")
    private int editStatus = 1;
	
    
    @ApiModelProperty(value = "老切片编号")
    private String imageCode;
    
    @ApiModelProperty(value = "老切片名称")
    private String imageName;
    
    @ApiModelProperty(value = "老切片url")
    private String thumbUrl;
    
	@ApiModelProperty(value = "编辑人名称")
	private String nickName;
    
}
