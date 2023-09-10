package cn.staitech.anno.domain;

import cn.staitech.common.core.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.Map;

@Api(value = "病例指标", tags = "病例指标")
public class Indicator extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(hidden = true, value = "病例指标id")
    private Integer indicatorId;
    
    @ApiModelProperty(value = "病例指标名称")
    private String indicatorName;
    
    @ApiModelProperty(hidden = true, value = "关联项目数量")
    private Integer projectTotal;
    
    @ApiModelProperty(value = "标签数量")
    private Integer annotationCategoryTotal;
    
    @ApiModelProperty(hidden = true, value = "创建者")
    private Long createBy;
    
    @ApiModelProperty(value = "创建者名称")
    private String userName;
    
    @ApiModelProperty(hidden = true, value = "用户id")
    private Long userId;
    
    @ApiModelProperty(hidden = true, value = "更新者")
    private Long updateBy;
    
    @ApiModelProperty(hidden = true, value = "更新时间")
    //    @ApiModelProperty(value = "", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    @ApiModelProperty(hidden = true, value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @ApiModelProperty(value = "请求参数")
    private Map<String, Object> params;
    
    //    @ApiModelProperty(value = "备注")
    @ApiModelProperty(hidden = true, value = "备注")
    private String remark;
    
    //    @ApiModelProperty(value = "搜索值")
    @ApiModelProperty(hidden = true, value = "搜索值")
    private String searchValue;

    @ApiModelProperty(value = "病例指标编号")
    private String number;

    @ApiModelProperty(hidden = true, value = "删除状态")
    private Integer delFlag;
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getIndicatorId() {
        return indicatorId;
    }
    
    public void setIndicatorId(Integer indicatorId) {
        this.indicatorId = indicatorId;
    }
    
    public String getIndicatorName() {
        return indicatorName;
    }
    
    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }
    
    public Integer getProjectTotal() {
        return projectTotal;
    }
    
    public void setProjectTotal(Integer projectTotal) {
        this.projectTotal = projectTotal;
    }
    
    public Integer getAnnotationCategoryTotal() {
        return annotationCategoryTotal;
    }
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag=delFlag;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number=number;
    }

    public void setAnnotationCategoryTotal(Integer annotationCategoryTotal) {
        this.annotationCategoryTotal = annotationCategoryTotal;
    }
    
    public Long getCreateBy() {
        return createBy;
    }
    
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    
    public Long getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("indicatorId", getIndicatorId())
                .append("indicatorName", getIndicatorName()).append("createTime", getCreateTime())
                .append("projectTotal", getProjectTotal())
                .append("annotationCategoryTotal", getAnnotationCategoryTotal()).append(" updateTime", getUpdateTime())
                .append("createBy", getCreateBy()).append("userId", getUserId()).append("userName", getUserName())
                .append("number", getNumber()).append("delFlag", getDelFlag())
                .append("updateBy", getUpdateBy()).toString();
    }
}
