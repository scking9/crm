package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.service.CusDevPlanService;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.vo.CusDevPlan;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 进入客户开发页面
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     *进入客户开发计划 详情/开发 页面
     * @param saleChanceId
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer saleChanceId, HttpServletRequest request) {
        // 通过ID查询营销机会数据
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
        // 设置请求域
        request.setAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }


    /**
     * 查询指定营销机会的客户开发项列表
     * @param saleChanceId
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> selectCusDevPlanList(Integer saleChanceId) {
        return cusDevPlanService.selectCusDevPlanList(saleChanceId);
    }

    /**
     * 添加客户开发计划
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success();
    }

    /**
     * 更新客户开发计划
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success();
    }

    /**
     * 打开 添加/更新客户开发计划 页面
     * @param cId
     * @return
     */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer cId, Integer saleChanceId, HttpServletRequest request) {
        // 判断cId是否为空。如果不为空，则为更新操作，通过id查询客户开发计划对象
        if (cId != null) {
            CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(cId);
            // 设置作用域
            request.setAttribute("cusDevPlan", cusDevPlan);

        }

        // 将营销机会的ID设置到作用域中
        request.setAttribute("saleChanceId", saleChanceId);

        return "cusDevPlan/add_update";
    }


    /**
     * 删除客户开发计划
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){

        cusDevPlanService.deleteCusDevPlan(id);

        return success();
    }



}
