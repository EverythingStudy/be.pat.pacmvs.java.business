package cn.staitech.anno.utils;

import cn.staitech.common.core.utils.StringUtils;
import cn.staitech.system.api.model.LoginUser;
import org.springframework.util.PatternMatchUtils;

import java.util.HashSet;
import java.util.Set;

import static cn.staitech.common.security.utils.SecurityUtils.getLoginUser;

/**
 * @author gjt.
 */
public class hasPerms {


    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";


    /**
     * 判断当前用户是否拥有该权限
     *
     * @param permission 权限字符
     * @return true || false
     */
    public static boolean permsVerify(String permission) {
        Set<String> authorities = hasPerms.getPermsList();
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 获取当前用户权限列表
     */
    public static Set<String> getPermsList() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getPermissions();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
}
