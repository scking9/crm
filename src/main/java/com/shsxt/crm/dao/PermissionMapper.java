package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    // 通过角色名查询所拥有的的权限
    List<Integer> queryPermissionByRole(Integer roleId);
}