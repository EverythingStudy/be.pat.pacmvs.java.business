package cn.staitech.anno.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tb_access_project_records
 * @author 
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccessProjectRecords implements Serializable {
    /**
     * 记录id
     */
    private Long recordsId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 访问时间
     */
    private Date accessTime;

}