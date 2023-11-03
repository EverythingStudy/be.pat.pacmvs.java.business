package cn.staitech.anno.vo.statistic;

import lombok.Data;

/**
 * @Author wudi
 * @Date 2023/11/2 10:35
 * @desc
 */
@Data
public class ExportSlideOrderProjectOut {
    private Long slideId;
    private String imageName;
    private String projectName;
    private String description;
    private Long markingTotal;


}
