package com.lln.agriculture.manage.mapper.admininfo;

import com.lln.agriculture.manage.domain.admininfo.AdminUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AdminUserMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(AdminUser record);

    /**
     * 通过用户名查询
     *
     * @param username 用户名
     * @return adminUser
     */
    AdminUser selectByUsername(String username);

    /**
     * 查询用户密码
     *
     * @param username 用户名
     * @return 密码
     */
    String selectAdminUserPassword(String username);

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param password 新密码
     * @return 是否修改成功
     */
    int changePassword(String username, String password);


    /**
     * 分页查询
     *
     * @param key 关键字
     * @return list
     */
    List<AdminUser> selectAllPage(String key);

    /**
     * 判断用户是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean isExist(String username);


    int insertAdminUserRole(Integer userId, Integer roleId);

    int updateByIdSelective(AdminUser adminUser);

    List<Integer> selectOriginalRoles(Integer userId);

    int deleteAdminUserRole(Integer userId, Integer roleId);

    int deleteAllAdminUserRole(Integer id);

    void deleteUser(Integer id);

    Integer selectIdByUsername(String username);

    String selectAvatar(String username);

    String selectUsernameById(Integer id);

    String selectDescriptionByUsername(String username);
}