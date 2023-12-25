package cn.staitech.anno.vo.labelprojectstatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageMarkingIn {
    /**
     * 项目id
     * */
    private Long projectId;

    /**
     * 标签id
     * */
    private Long categoryId;

    /**
     * 标注类别
     * */
    private String annotationType;
}
