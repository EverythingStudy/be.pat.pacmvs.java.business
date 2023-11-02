package cn.staitech.anno.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * aipre_folder
 * @author 
 */
@Data
public class AipreFolder implements Serializable {
    /**
     * 文件夹id
     */
    private Long folderId;

    /**
     * 文件夹名称
     */
    private String folderName;

    /**
     * 文件大小
     */
    private Long folderSize;

    /**
     * 文件夹url地址
     */
    private String folderUrl;

    /**
     * 压缩包id
     */
    private Long filesId;

    /**
     * 机构id
     */
    private Long organizationId;

    /**
     * 创建者id
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者id
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除（0删除，1未删除）
     */
    private String deleteFlag;

    private static final long serialVersionUID = 1L;
}