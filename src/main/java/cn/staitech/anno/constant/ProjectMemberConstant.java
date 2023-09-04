package cn.staitech.anno.constant;

/**
 * 项目成员配置常量
 *
 * @author wangfeng
 * @date 2023/04/04 10:30
 */
public final class ProjectMemberConstant {
    public static final String INSERT_SUCCESS = "添加用户成功！";
    public static final String DELETE_SUCCESS = "删除用户成功！";
    public static final String UPDATE_SUCCESS = "修改用户成功！";
    public static final String UPDATE_FAILED = "修改用户失败！";
    public static final String DISALLOW_NOT_PROJECT = "项目不可为空";
    public static final String DISALLOW_DELETE_REPRESENTATION = " 不允许删除项目代表！";
    public static final String DISALLOW_PROJECT_NOT_EXIST = "项目尚未添加";
    public static final String DISALLOW_CONTRIBUTOR = "项目贡献者没有查询、添加、删除、修改成员的权限";
    public static final String DISALLOW_MANAGER = "项目管理者没有添加、修改、删除项目代表的权限";
    public static final String DISALLOW_REPRESENTATION_TO_CONTRIBUTOR = "不允许直接修改项目贡献者角色为项目代表角色";
    public static final String REPRESENTATION_MUST_HAS_ONE = "项目代表至少需要1名";
}
