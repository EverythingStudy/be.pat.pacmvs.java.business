package cn.staitech.anno.vo.special;

import cn.staitech.system.api.domain.SysDept;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description ：Treeselect树结构实体类
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：TreeSelect
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/6/8 星期四 2:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @ApiModelProperty(value = "节点ID")
    private Long menuId;

    /**
     * 节点名称
     */
    @ApiModelProperty(value = "节点名称")
    private String menuName;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(value = "子节点")
    private List<TreeSelect> children;

    public TreeSelect(SysDept dept) {
        this.menuId = dept.getDeptId();
        this.menuName = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SpecialMenu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}
