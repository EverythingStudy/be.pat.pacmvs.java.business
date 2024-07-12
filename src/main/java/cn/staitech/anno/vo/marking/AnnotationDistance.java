package cn.staitech.anno.vo.marking;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class AnnotationDistance {

    private String contourOne;

    private String contourTwo;

    private Double meanDistance;

    private Double minDistance;

}
