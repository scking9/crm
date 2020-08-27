package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.RoleMapper;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有的角色列表
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        // 判断角色名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        // 通过角色名查询角色对象
        Role temp = roleMapper.queryRoleByName(role.getRoleName());
        // 判断角色对象是否为空
        AssertUtil.isTrue(temp != null, "角色名称已存在，请重试！");
        // 设置默认值
        role.setUpdateDate(new Date());
        role.setCreateDate(new Date());
        role.setIsValid(1);

        // 添加操作
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1, "角色数据添加失败！");
    }

    /**
     * 更新角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        // 判断角色名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        // 通过角色名查询角色对象
        Role temp = roleMapper.queryRoleByName(role.getRoleName());
        // 判断角色对象是否存在，且id相等
        AssertUtil.isTrue(temp != null && !(role.getId().equals(temp.getId())), "角色名称已存在，请重试！");
        // 设置默认值
        role.setUpdateDate(new Date());

        //更新操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "角色数据更新失败！");
    }

    /**
     * 删除角色
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        AssertUtil.isTrue(null == roleId || roleMapper.selectByPrimaryKey(roleId) == null, "待删除记录不存在！");
        AssertUtil.isTrue(roleMapper.deleteRole(roleId) != 1, "角色数据删除失败！");
    }
}
