package cn.staitech.anno.domain.notice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author wudi
 * @Date 2023/6/26 18:04
 * @desc
 */
@Data
@TableName("sys_notice")
public class Notice {
    /**
     * NoticeId
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;
    /**
     * 公告标题
     */
    @ApiModelProperty(name = "公告标题", notes = "")
    private String noticeTitle;
    /**
     * 公告类型（1消息 2公告）
     */
    @ApiModelProperty(name = "公告类型（1消息 2公告）", notes = "")
    private String noticeType;
    /**
     * 消息内容
     */
    @ApiModelProperty(name = "消息内容", notes = "")
    private String noticeContent;
    /**
     * 消息状态（0未读 1已读）
     */
    @ApiModelProperty(name = "消息状态（0未读 1已读）", notes = "")
    private String status;
    /**
     * 消息接收者
     */
    @ApiModelProperty(name = "消息接收者", notes = "")
    private Long recipient;
    /**
     * 创建者
     */
    @ApiModelProperty(name = "创建者", notes = "")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间", notes = "")
    private Date createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(name = "更新者", notes = "")
    private Long updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(name = "更新时间", notes = "")
    private Date updateTime;
    /**
     * 备注
     */
    @ApiModelProperty(name = "备注", notes = "")
    private String remark;

}
