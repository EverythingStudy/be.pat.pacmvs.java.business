package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 考核项目应标个数
 * </p>
 *
 * @author gjt
 * @since 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_project_marks_rel")
public class ProjectMarksRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 应标个数
     */
    private Long shouldMarks;


}
