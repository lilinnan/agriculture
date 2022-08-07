package com.lln.agriculture.manage.mapper.authority;

import com.lln.agriculture.manage.domain.authority.AdminRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminRoleMapper {
    /**
     * insert record to table
     *
     * @param adminRole the record
     * @return insert count
     */
    int insert(AdminRole adminRole);

    /**
     * 更新信息
     *
     * @param adminRole 角色
     * @return 受影响行数
     */
    int updateByIdSelective(AdminRole adminRole);

    /**
     * 插入关联信息
     *
     * @param roleId      角色Id
     * @param authorityId 权限Id
     * @return insert count
     */
    int insertAdminRoleAuthority(int roleId, int authorityId);


    /**
     * 删除关联信息
     *
     * @param roleId      角色id
     * @param authorityId 权限id
     * @return 受影响行数
     */
    int deleteAdminRoleAuthority(int roleId, int authorityId);

    /**
     * 删除一个角色的全部关联信息
     *
     * @param roleId 角色id
     * @return 受影响行数
     */
    int deleteAllAdminRoleAuthority(int roleId);

    /**
     * 查询角色是否存在
     *
     * @param role 角色名称
     * @return 存在否
     */
    boolean isExist(String role);


    /**
     * 查询全部，切记不能分页
     *
     * @return 角色列表
     */
    List<AdminRole> selectAll();


    /**
     * 查询简易版信息，仅仅用来搜索与简易使用
     *
     * @return listAdminRole
     */
    List<AdminRole> selectAllSimple(String key);


    /**
     * 分页查询
     *
     * @param key key
     * @return 角色列表
     */
    List<AdminRole> selectAllPage(String key);


    /**
     * 查询原来拥有的权限
     *
     * @return 原来拥有的权限ID
     */
    List<Integer> selectOriginalAuthorities(int roleId);


    /**
     * 删除一个角色
     *
     * @param id 角色id
     * @return 受影响行数
     */
    int deleteRole(int id);


    Integer selectIdByRoleName(String role);

    String selectRoleNameByRoleId(Integer roleId);
}