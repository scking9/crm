package com.shsxt.crm;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 *      方法返回值
 *          视图
 *          JSON
 *
 *       判断方法的返回值？
 *          判断方法上是否设置了@ResponseBody注解；如果设置了，表示返回JSOn；否则返回视图
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object handler, Exception e) {


        /**
         * 捕获非法访问拦截抛出的异常
         */
        if (e instanceof NoLoginException) {
            // 拦截跳转到登录页面
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/index");
            return mv;
        }



        // 默认的异常处理
        ModelAndView modelAndView = new ModelAndView();
        // 设置视图名
        modelAndView.setViewName("error"); // error.ftl
        // 设置错误信息
        modelAndView.addObject("msg","系统异常，请重试！");

        // 验证handler参数的类型
        if (handler instanceof HandlerMethod) {
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 判断方法上是否设置了@ResponseBody注解（如果设置了，则返回ResponseBody对象；否则返回null）
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            // 判断ResponseBody对象是否为空
            if (responseBody == null) {
                /**
                 * 返回视图
                 */
                // 判断自定义异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }

                return modelAndView;

            } else {
                /**
                 * 返回JSON
                 */
                // 准备ResultInfo对象
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("网络异常，请重试！");

                // 自定义异常
                if (e instanceof ParamsException) {
                    ParamsException p = (ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                // 设置响应类型及编码格式
                httpServletResponse.setContentType("application/json;charset=UTF-8");

                try {
                    // 得到字符输出流
                    PrintWriter out = httpServletResponse.getWriter();
                    // 将resultInfo对象转换成json字符串
                    String json = JSON.toJSONString(resultInfo);
                    // 输出字符串
                    out.write(json);
                    // 刷新
                    out.flush();
                    // 关闭
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

        }
        return modelAndView;
    }
}
