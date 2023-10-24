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
public class SysDictResultVo {


    @ApiModelProperty(name = "ddefinitionList", value = "ddefinitionList")
    List<SysDictDataVo> ddefinitionList = new ArrayList<>();


    @ApiModelProperty(name = "gradeList", value = "gradeList")
    List<SysDictDataVo> gradeList = new ArrayList<>();


    @ApiModelProperty(name = "visceraList", value = "visceraList")
    List<VisceraVo> visceraList = new ArrayList<>();
}
