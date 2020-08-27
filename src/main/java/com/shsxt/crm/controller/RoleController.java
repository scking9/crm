package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.query.RoleQuery;
import com.shsxt.crm.service.RoleService;
import com.shsxt.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询所有的角色列表
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return  roleService.queryAllRoles(userId);
    }


    /**
     * 多条件分页查询角色列表
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery) {
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 进入角色管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }


    /**
     * 进入添加或修改觉得页面
     * @param roleId
     * @param request
     * @return
     */
    @RequestMapping("toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(Integer roleId, HttpServletRequest request) {

        // 判断非空，查询角色对象
        if (roleId != null){
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role",role);
        }

        return "role/add_update";
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){

        roleService.addRole(role);

        return success();
    }


    /**
     *更新角色
     * @param role
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){

        roleService.updateRole(role);

        return success();
    }



    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId){

        roleService.deleteRole(roleId);

        return success();
    }

    /**
     * 进入授权页面
     * @param roleId
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request) {
        // 将角色ID设置到请求域中，页面中可以得知当前是给什么角色授权
        request.setAttribute("roleId", roleId);

        return "role/grant";
    }


}
