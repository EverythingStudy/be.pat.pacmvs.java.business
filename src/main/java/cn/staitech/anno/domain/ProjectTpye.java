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
@TableName(value = "tb_project_tpye")
@Data
public class ProjectTpye implements Serializable {
    /**
     * 项目类型ID
     */
    @TableId(value = "project_tpye_id", type = IdType.NONE)
    private String projectTpyeId;

    /**
     * 项目类型名称
     */
    private String projectTpyeName;

}
