package cn.staitech.anno.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * aipre_folder
 *
 * @author
 */
@Data
@TableName("aipre_folder")
public class Folder implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 文件夹id
     */
    @TableId(value = "folder_id", type = IdType.AUTO)
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
     * 机构ID
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
}