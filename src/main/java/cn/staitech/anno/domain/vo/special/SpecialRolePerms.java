package cn.staitech.anno.domain.vo.special;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description ：
 * @Project ：be.PathMedics.SaaS.java.business
 * @File ：SpecialRolePerms
 * @Author ：yanglei
 * @Email ：yangl@staitech.cn
 * @Date ：2023/7/14 星期五 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialRolePerms implements Serializable {
    private static final long serialVersionUID = -2357115613252805628L;
    private Long roleId;
    private Long menuId;
    private String perms;
}
