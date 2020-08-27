package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件分页查询
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String ,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会数据
     * @param saleChance
     * @param request
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance( SaleChance saleChance, HttpServletRequest request){
        saleChanceService.addSaleChance(saleChance,request);
        return success();
    }

    /**
     * 更新营销机会数据
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {

        saleChanceService.updateSaleChance(saleChance);

        return success();
    }


    /**
     * 进入营销机会添加/修改页面
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request){
        // 如果saleChanceId不为空，则表示当前是更新操作
        if (saleChanceId != null) {
            // 通过Id查询营销机会对象
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            // 设置数据到请求域
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 删除营销机会数据
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success();
    }

    /**
     * 修改营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @PostMapping("updateDevResult")
    @ResponseBody
    public ResultInfo updateDevResult(Integer id, Integer devResult) {
        saleChanceService.updateDevResult(id, devResult);
        return success();
    }
}
