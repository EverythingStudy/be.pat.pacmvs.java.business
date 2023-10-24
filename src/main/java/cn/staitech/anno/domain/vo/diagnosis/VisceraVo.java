package cn.staitech.anno.domain.vo.diagnosis;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglibei
 * @version V1.0
 * @ClassName: SysDictData
 * @Description:
 * @date 2023年6月27日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisceraVo extends SysDictDataVo {

    @ApiModelProperty(name = "positionList", value = "positionList")
    List<SysDictDataVo> positionList = new ArrayList<>();

    @ApiModelProperty(name = "lesionList", value = "lesionList")
    List<SysDictDataVo> lesionList = new ArrayList<>();

}
