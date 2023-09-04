package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description ：专题统计查询
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialStatisticsVO
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/15 星期四 22:57
 */
@Data
public class SpecialStatisticsQueryVO implements Serializable {
    private static final long serialVersionUID = 7443096931013830470L;
    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "专题编号")
    private String specialNumber;

    @ApiModelProperty(value = "专题名称")
    private String specialName;

    @ApiModelProperty(value = "当前页数")
    private int pageNum;

    @ApiModelProperty(value = "每页数据条数")
    private int pageSize;

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
