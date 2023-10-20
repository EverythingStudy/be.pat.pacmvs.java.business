package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangf
 * @TableName tb_project_tpye
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_project_type")
@Data
public class ProjectType implements Serializable {
    /**
     * 项目类型ID
     */
    @TableId(value = "project_type_id", type = IdType.NONE)
    private String projectTypeId;

    /**
     * 项目类型名称
     */
    private String projectTypeName;

    /**
     * 项目类型名称 en
     */
    private String projectTypeNameEn;
}
