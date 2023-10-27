package cn.staitech.anno.domain.vo.special;

import cn.staitech.common.core.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description ：路由显示信息
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：MetaVo
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/8 星期四 3:57
 */
@Data
public class MetaVo {
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    @ApiModelProperty(value = "置该路由在侧边栏和面包屑中展示的名字")
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    @ApiModelProperty(value = "设置该路由的图标，对应路径src/assets/icons/svg")
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    @ApiModelProperty(value = "设置为true，则不会被 <keep-alive>缓存")
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    @ApiModelProperty(value = "内链地址（http(s)://开头）")
    private String link;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    public MetaVo() {
    }

    public MetaVo(String title, String icon, Long menuId, String perms) {
        this.title = title;
        this.icon = icon;
        this.menuId = menuId;
        this.perms = perms;
    }

    public MetaVo(String title, String icon, boolean noCache, Long menuId, String perms) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.menuId = menuId;
        this.perms = perms;
    }

    public MetaVo(String title, String icon, String link, Long menuId, String perms) {
        this.title = title;
        this.icon = icon;
        this.link = link;
        this.menuId = menuId;
        this.perms = perms;
    }

    public MetaVo(String title, String icon, boolean noCache, String link, Long menuId, String perms) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.ishttp(link)) {
            this.link = link;
        }
        this.menuId = menuId;
        this.perms = perms;
    }
}
