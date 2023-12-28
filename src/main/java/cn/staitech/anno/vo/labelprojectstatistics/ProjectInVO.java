package cn.staitech.anno.vo.labelprojectstatistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProjectInVO {
    /**
     * 用户id
     * */
    private Long userId;

    /**
     * 机构id
     * */
    private Long organizationId;

    /**
     * 项目类型（1标注，2评审，3标准验证集，4标准考核，6图像拼接，7算法预测）
     * */
    private String projectType;

    private Integer status;

    private List<Integer> statusList;
}
