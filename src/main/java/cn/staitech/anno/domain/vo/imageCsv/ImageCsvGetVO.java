package cn.staitech.anno.domain.vo.imageCsv;

import cn.staitech.anno.domain.Pager;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: wangfeng
 * @create: 2023-09-20 17:46:52
 * @Description: Image CSV查询条件
 * 切片编号 组别 性别 病变程度 病变类型
 */
@Data
public class ImageCsvGetVO extends Pager implements Serializable {

    /**
     * 切片编号
     */
    private String imageName;

    /**
     * 组别
     */
    private String groupName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 病变类型1
     */
    private String lesionType;

    /**
     * 病变程度1
     */
    private String lesionDegree;
}
