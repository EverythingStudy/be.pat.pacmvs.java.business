package cn.staitech.anno.domain.vo.special;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description ：路由配置信息
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：RouterVo
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/8 星期四 3:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    /**
     * 路由名字
     */
    @ApiModelProperty(value = "路由名字")
    private String name;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址")
    private String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    @ApiModelProperty(value = "是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现")
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    @ApiModelProperty(value = "重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    /**
     * 组件地址
     */
    @ApiModelProperty(value = "组件地址")
    private String component;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    @ApiModelProperty(value = "路由参数：如 {\"id\": 1, \"name\": \"ry\"}")
    private String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    @ApiModelProperty(value = "当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面")
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    @ApiModelProperty(value = "其他元素")
    private MetaVo meta;

    /**
     * 子路由
     */
    private List<RouterVo> children;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    @ApiModelProperty(value = "权限标识")
    private String perms;
}
