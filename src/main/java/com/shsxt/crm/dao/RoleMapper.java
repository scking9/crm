package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {
    // 查询所有的角色列表
    List<Map<String, Object>> queryAllRoles(Integer userId);

    // 通过角色名称查询角色对象
    Role queryRoleByName(String roleName);

    // 删除角色
    int deleteRole(Integer roleId);
}