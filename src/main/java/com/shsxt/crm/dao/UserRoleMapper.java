package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {
    // 通过用户ID查询用户关联的角色数量
    Integer selectUserRoleCountByUserId(Integer userId);

    // 通过用户ID删除用户角色关联信息
    int deleteRelationByUserId(Integer userId);
}