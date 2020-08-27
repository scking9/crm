package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.enums.DevResult;
import com.shsxt.crm.enums.StateStatus;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.CookieUtil;
import com.shsxt.crm.utils.PhoneUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 通过指定条件分页查询营销机会列表
     * @param saleChanceQuery
     * @return
     */
    public Map<String ,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String ,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //得到分页数据
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal()); //总记录数
        map.put("data",pageInfo.getList());  //当前页面显示的数据列表
        return map;
    }

    /**
     * 添加营销机会数据
        1. 参数校验 （SaleChance对象）
            customerName客户名     非空
            linkMan联系人          非空
            linkPhone手机号        非空，格式正确
        2. 设置参数的默认值
            createDate创建时间      默认当前系统时间
            updateDate修改时间      默认当前系统时间
            isValid是否有效         默认1=有效（1=有效，0=无效）
            createMan创建人         默认是当前登录用户名（用户名称）
            assignMan分配人
                如果有值，表示已分配（用户ID）
                    state分配状态       1=已分配 （1=已分配，0=未分配）
                    assignTime分配时间  默认当前系统时间
                    devResult开发状态   1=开发中 （0=未开发，1=开发中，2=开发成功，3=开发失败）
                如果没值，表示未分配（null）
                    state分配状态       0=未分配 （1=已分配，0=未分配）
                    assignTime分配时间  null
                    devResult开发状态   0=未开发 （0=未开发，1=开发中，2=开发成功，3=开发失败）
        3. 执行添加操作，判断受影响的行数
     * @param saleChance
     * @param request
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance, HttpServletRequest request){
        //验证营销机会数据的参数
        checkSaleChanceParams(saleChance);
        /*设置参数默认值*/
        //createDate创建时间      默认当前系统时间
        saleChance.setCreateDate(new Date());
        //updateDate修改时间      默认当前系统时间
        saleChance.setUpdateDate(new Date());
        //isValid是否有效         默认1=有效（1=有效，0=无效）
        saleChance.setIsValid(1);
        //createMan创建人         默认是当前登录用户名（用户名称）
        //获取cookie中的用户名
        String userName = CookieUtil.getCookieValue(request,"userName");
        saleChance.setCreateMan(userName);

        /*判断是否设置了分配人*/
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //未分配
            //state分配状态       0=未分配 （1=已分配，0=未分配）
            saleChance.setState(0);
            //assignTime分配时间  null
            saleChance.setAssignTime(null);
            //devResult开发状态   0=未开发 （0=未开发，1=开发中，2=开发成功，3=开发失败）
            saleChance.setDevResult(0);
        }else {
            //已分配
            //state分配状态       1=已分配 （1=已分配，0=未分配）
            saleChance.setState(1);
            //assignTime分配时间  默认当前系统时间
            saleChance.setAssignTime(new Date());
            //devResult开发状态   1=开发中 （0=未开发，1=开发中，2=开发成功，3=开发失败）
            saleChance.setDevResult(1);
        }
        /*执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"营销机会数据添加失败！");
    }
    private void checkSaleChanceParams(SaleChance saleChance) {
        //customerName客户名     非空
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getCustomerName()),"客户名不能为空");
        //linkMan联系人          非空
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getLinkMan()),"联系人不能为空");
        //linkPhone手机号        非空，格式正确
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getLinkPhone()),"手机号不能为空");
        //手机号格式
        AssertUtil.isTrue(!PhoneUtil.isMobile(saleChance.getLinkPhone()),"手机号格式不合法");
    }

    /**
     * 营销机会数据更新
        1. 参数校验
            id营销机会ID           非空，数据库中数据存在
            customerName客户名     非空
            linkMan联系人          非空
            linkPhone手机号        非空，格式正确
        2. 设置参数的默认值
            updateDate修改时间      默认当前系统时间
            assignMan分配人
                1. 改之前未分配  改之后未分配

                2. 改之前未分配  改之后已分配
                    state分配状态       1=已分配 （1=已分配，0=未分配）
                    assignTime分配时间  默认当前系统时间
                    devResult开发状态   1=开发中 （0=未开发，1=开发中，2=开发成功，3=开发失败）
                3. 改之前已分配   改之后未分配
                    state分配状态       0=未分配 （1=已分配，0=未分配）
                    assignTime分配时间  null
                    devResult开发状态   0=未开发 （0=未开发，1=开发中，2=开发成功，3=开发失败）
                4. 改之前已分配   改之后已分配
                    判断修改前后是否是同一个分配人，如果不是同一个人，更新分配时间
        3. 执行更新操作，判断受影响的行数
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        /*参数校验*/
        //id营销机会ID           非空，数据库中数据存在
        AssertUtil.isTrue(null == saleChance.getId(), "系统异常，请重试！");
        // 通过id查询营销机会对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断是否存在
        AssertUtil.isTrue(null == temp, "待更新数据不存在");
        //参数校验
        checkSaleChanceParams(saleChance);

        // 设置参数的默认值
        saleChance.setUpdateDate(new Date());

        // 判断分配人
        if (StringUtils.isBlank(temp.getAssignMan())) { // 修改前未分配
            // 修改后已分配
            if (StringUtils.isNotBlank(saleChance.getAssignMan())) { // 修改后指定了分配人
                // state分配状态       1=已分配 （1=已分配，0=未分配）
                saleChance.setState(StateStatus.STATED.getType());
                // assignTime分配时间  默认当前系统时间
                saleChance.setAssignTime(new Date());
                // devResult开发状态   1=开发中 （0=未开发，1=开发中，2=开发成功，3=开发失败）
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }

        } else { // 修改前已分配

            // 如果修改后未分配
            if (StringUtils.isBlank(saleChance.getAssignMan())) { // 修改后未分配
                // state分配状态       0=未分配 （1=已分配，0=未分配）
                saleChance.setState(StateStatus.UNSTATE.getType());
                // assignTime分配时间  null
                saleChance.setAssignTime(null);
                // devResult开发状态   0=未开发 （0=未开发，1=开发中，2=开发成功，3=开发失败）
                saleChance.setDevResult(DevResult.UNDEV.getStatus());

            } else { // 修改后已分配
                // 判断修改前后是否是同一个用户 （不同分配人）
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    // 更新分配时间
                    saleChance.setAssignTime(new Date());
                } else {
                    // 设置为修改前的分配时间 （相同分配人）
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        // 3. 执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "营销机会数据更新失败！");
    }

    /**
     * 删除营销机会数据
     * @param ids
     */
    public void deleteSaleChance(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "请选择要删除的记录！");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "营销机会数据删除失败！");
    }

    /**
     * 修改营销机会的状态
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDevResult(Integer id, Integer devResult) {
        // 判断参数是否为空
        AssertUtil.isTrue(null ==id || null == devResult, "数据异常，请重试！");
        // 通过id查询营销机会数据对象
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        // 判断对象是否为空
        AssertUtil.isTrue(null==saleChance, "营销机会开发状态修改失败！");
        // 设置开发状态
        saleChance.setDevResult(devResult);
        saleChance.setUpdateDate(new Date());
        // 执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "营销机会开发状态修改失败！");
    }
}
