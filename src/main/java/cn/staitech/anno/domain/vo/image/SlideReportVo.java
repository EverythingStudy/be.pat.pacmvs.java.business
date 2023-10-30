package cn.staitech.anno.domain.vo.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mugw
 * @version 1.0
 * @description 组内切片报表查询对象
 * @date 2023/6/13 17:22:34
 */
@Data
public class SlideReportVo extends SubImageVo {

    List<Map> tasks = new ArrayList<>();
    /**
     * 切片id
     */
    private Long slideId;
    /**
     * 人工诊断状态：0未诊断，1已诊断
     */
    private int diagnosis;
    /**
     * AI分析状态：0:待分析（初始状态）、1:AI分析中、2:AI分析成功、3:AI分析失败
     */
    private int aiAnalyzed;
    /**
     * AI筛阴：1:阴性、2:阳性、0:未筛、3:未知
     */
    private int aiCheck;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 当前操作人姓名
     */
    private String operatorName;
    /**
     * 当前操作人id
     */
    private Long operatorId;
    /**
     * 受否可编辑状态：0：可编辑，1：不可编辑
     */
    private Integer status;
    /**
     * 脏器类型
     */
    @ApiModelProperty(name = "脏器类型code")
    private Long viscusCode;
    /**
     * 系统类型
     */
    private int systemCode;

    public void addTask(Map task) {
        tasks.add(task);
    }

}
