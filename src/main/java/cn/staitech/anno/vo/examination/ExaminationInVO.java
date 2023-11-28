package cn.staitech.anno.vo.examination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExaminationInVO {

    private Long questionProjectId;

    private String imageName;
}
