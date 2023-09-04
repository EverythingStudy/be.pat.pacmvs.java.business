package cn.staitech.anno.domain;

import cn.staitech.anno.domain.vo.measurevo.MeasureJsonVO;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangf
 */
@Api(value = "测量Json", tags = "测量Json")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MeasureJson {

    /**
     * 图像ID
     */
    private Long imageId = null;

    /**
     * `measure_type'类型(0:标注）List
     */
    private List<MeasureJsonVO> subList= null;

    /**
     * `measure_type'类型(1:测量）List
     */
    private List<MeasureJsonVO> parentList= null;
}
