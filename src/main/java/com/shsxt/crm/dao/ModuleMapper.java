package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.model.TreeModel;
import com.shsxt.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    // 按照指定的数据格式，查询所有的资源列表
    List<TreeModel> queryAllModules();
}