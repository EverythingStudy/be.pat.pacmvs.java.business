package cn.staitech.anno.vo.accessprojectrecords;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccessProjectRecordsIn {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 访问时间
     */
    private Map<String, Object> timeParams;
}
