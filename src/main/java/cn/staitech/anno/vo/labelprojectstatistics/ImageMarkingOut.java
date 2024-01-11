package cn.staitech.anno.vo.labelprojectstatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageMarkingOut {
    /**
     * 图像数量
     */
    private Integer imageNum;

    /**
     * 标注数量
     */
    private Integer markingNum;

    /**
     * 项目id
     */
    private Long projectId;

    private Long categoryId;

    private Long indicatorId;

    private Long slideId;
}
