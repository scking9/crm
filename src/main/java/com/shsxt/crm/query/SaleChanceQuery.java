package com.shsxt.crm.query;

import com.shsxt.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    //客户名称
    private String customerName;
    //创建人
    private String createMan;
    //分配状态
    private Integer state;

    private Integer assignMan; // 指派人（如果查询的是客户开发计划，则获取的是分配给当前登录用户的营销机会数据）
    private Integer devResult; // 开发状态  0=未开发，10开发中，2=开发成功，3-开发失败

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
