package cn.staitech.anno.domain.vo.special;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description ：专题菜单权限查询表
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialMenu
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/7 星期三 19:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialMenuQuery implements Serializable {
    private static final long serialVersionUID = -9050492950947463156L;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

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
     * 请求参数
     */
    // 用法 params.beginTime | params.endTime
    @ApiModelProperty(value = "请求参数", hidden = true)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }
}
