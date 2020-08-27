package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.model.TreeModel;
import com.shsxt.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 按照指定的数据格式，查询所有的资源列表
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){

        // 所有资源列表
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();

        // 查询指定角色拥有的资源列表
        List<Integer> roleHasPermissionIds =  permissionMapper.queryPermissionByRole(roleId);

        // 判断角色ID是否为空
        if (roleId != null) {
            // 判断当前资源是否被选中
            for (TreeModel tree:treeModelList) {
                // 判断当前角色是否包含该资源
                if (roleHasPermissionIds.contains(tree.getId())) {
                    tree.setChecked(true);
                }
            }
        }
        return treeModelList;
    }

}
