package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.CusDevPlanMapper;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 通过营销机会ID查询客户开发计划列表
     * @param saleChanceId
     * @return
     */
    public Map<String, Object> selectCusDevPlanList(Integer saleChanceId) {
        Map<String, Object> map = new HashMap<>();

       // 查询指定营销机会的客户开发计划列表
        List<SaleChance> list = cusDevPlanMapper.selectCusDevPlanList(saleChanceId);

        map.put("code",0);
        map.put("msg","");
        map.put("count",list.size()); // 总记录数
        map.put("data",list); // 当前页显示的数据列表
        return map;
    }

    /**
     * 添加客户开发计划
     *      1. 参数校验
     *          saleChanceId营销机会ID  非空，且数据存在
     *          planItem开发项名称       非空
     *          planDate开发时间        非空
     *          exeAffect开发影响       非空
     *      2. 设置默认值
     *          createDate、updateDate、isValid
     *      3. 执行添加操作
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        // 参数校验
        checkCusDevPlanParams(cusDevPlan);
        // 设置默认值
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setIsValid(1);
        // 添加操作
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1, "客户开发计划添加失败！");
    }

    /**
     * 参数校验
     *       saleChanceId营销机会ID  非空，且数据存在
     *       planItem开发项名称       非空
     *       planDate开发时间        非空
     *      exeAffect开发影响       非空
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(null == cusDevPlan.getSaleChanceId(),"数据异常，请重试！");
        AssertUtil.isTrue(null== saleChanceMapper.selectByPrimaryKey(cusDevPlan.getSaleChanceId()), "营销机会数据不存在！");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "计划项不能为空！");
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(), "计划时间不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getExeAffect()), "执行效果不能为空！");
    }

    /**
     * 修改客户开发计划
     *      1. 参数校验
     *          id                      非空判断，且数据存在
     *          saleChanceId营销机会ID  非空，且数据存在
     *          planItem开发项名称       非空
     *          planDate开发时间        非空
     *          exeAffect开发影响       非空
     *      2. 设置默认值
     *          updateDate
     *      3. 执行修改操作
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  updateCusDevPlan(CusDevPlan cusDevPlan) {
        // 判断id是否存在，且数据存在
        AssertUtil.isTrue(null == cusDevPlan.getId(), "数据异常，请重试！");
        AssertUtil.isTrue(null == cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()), "待更新记录不存在！");
        // 参数校验
        checkCusDevPlanParams(cusDevPlan);

        // 设置默认值
        cusDevPlan.setUpdateDate(new Date());

        // 更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "客户开发计划更新失败！");
    }


    /**
     * 删除客户开发计划
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id) {
        // 判断ID是否为空
        AssertUtil.isTrue(null == id, "数据异常，请重试！");
        // 通过ID查询对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        // 设置为删除状态
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        // 执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "客户开发计划数据删除失败！");

    }
}
