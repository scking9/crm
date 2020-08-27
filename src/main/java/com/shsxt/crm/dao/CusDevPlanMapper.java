package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseMapper;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;

import java.util.List;

public interface CusDevPlanMapper extends BaseMapper<CusDevPlan, Integer> {
    // 查询指定营销机会的客户开发计划列表
    List<SaleChance> selectCusDevPlanList(Integer saleChanceId);
}