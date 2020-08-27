package com.shsxt.crm.controller;

import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     1. 通过形参接收请求参数
     2. 调用Service层的方法，得到登录结果 （通过try catch捕获service层的异常）
     3. 响应结果给客户端 （返回resultInfo）
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 调用Service层的方法，得到登录结果 （通过try catch捕获service层的异常）
        UserModel userModel = userService.userLogin(userName,userPwd);
        resultInfo.setResult(userModel);
        return  resultInfo;
    }

    /**
     * 修改用户密码
     *  1. 通过形参接收参数
     *  2. 从cookie中获取当前登录用户的ID
     *  3. 调用Service层的更新方法（通过try catch捕获异常）
     *  4. 返回resultInfo对象
     * @param oldPwd
     * @param newPwd
     * @param againPwd
     * @param request
     * @return
     */
    @PostMapping("updateUserPwd")
    @ResponseBody
    public ResultInfo updateUserPwd(String oldPwd, String newPwd, String againPwd, HttpServletRequest request){
        //从cookie中获取当前登录用户的ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        ResultInfo resultInfo = new ResultInfo();
        userService.updateUserPassWord(oldPwd,newPwd,againPwd,userId);
        return resultInfo;
    }
    /**
     * 进入修改密码的页面
     * @return
     */
    @RequestMapping("toPassword")
    public String toUpdateUserPwd(){
        return "user/password";
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    /**
     * 查询用户列表
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    /**
     * 添加用户
     * @param user
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success();
    }


    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success();
    }


    /**
     * 进入添加/更新用户页面
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer userId, HttpServletRequest request) {

        // 判断用户Id是否为空
        if (userId != null) {
            User user = userService.selectByPrimaryKey(userId);
            request.setAttribute("userInfo", user);
        }

        return "user/add_update";
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {

        userService.deleteUser(ids);

        return success();
    }
}
