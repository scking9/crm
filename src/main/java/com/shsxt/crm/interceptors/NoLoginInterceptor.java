package com.shsxt.crm.interceptors;

import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法请求拦截
 *      判断请求是否合法
 *          1. Cokie中是否有userIdStr
 *              如果不存在，则抛出异常
 *              如果存在，则放行
 *          2. userIdStr存在，且数据库中存在对应的用户数据
 *              如果用户对象存在，则放行
 *              如果用户对象不存在，抛出异常
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserMapper userMapper;

    /**
     * 在目标方法执行前进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从cookie中获取用户对象
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 通过id查询用户对象
        User user = userMapper.selectByPrimaryKey(userId);

        // 如果id不存在或用户对象不存在
        if (null == userId || null == user) {
            // 抛出异常
            throw new NoLoginException(); // 交给全局异常处理器
        }

        // 放行
        return true;
    }
}
