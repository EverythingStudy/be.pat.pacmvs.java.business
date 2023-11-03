package cn.staitech.anno.vo.special;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description ：专题菜单权限表
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialMenu
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/7 星期三 19:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialMenu implements Serializable {
    private static final long serialVersionUID = -9050492950947463156L;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    /**
     * 父菜单ID
     */
    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    /**
     * 父菜单名称
     */
    @ApiModelProperty(value = "父菜单名称")
    private String parentName;


    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @ApiModelProperty(value = "组件路径")
    private String component;

    /**
     * 路由参数
     */
    @ApiModelProperty(value = "路由参数")
    private String query;

    /**
     * 是否为外链（0是 1否）
     */
    @ApiModelProperty(value = "是否为外链（0是 1否）")
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    private String isCache;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @ApiModelProperty(value = "类型（M目录 C菜单 F按钮）")
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @ApiModelProperty(value = "显示状态（0显示 1隐藏）")
    private String visible;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @ApiModelProperty(value = "菜单状态（0显示 1隐藏）")
    private String status;

    /**
     * 权限字符串
     */
    @ApiModelProperty(value = "权限字符串")
    private String perms;

    /**
     * 菜单图标
     */
    @ApiModelProperty(value = "菜单图标")
    private String icon;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private Long createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Size(min = 0, max = 500, message = "备注长度不能超过500个字符")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 子菜单
     */
    private List<SpecialMenu> children = new ArrayList<>();

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数", hidden = true)
    private Map<String, Object> params;
}
