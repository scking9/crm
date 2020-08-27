package com.shsxt.crm.config;

import com.shsxt.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    // 定义拦截器
    @Bean
    public NoLoginInterceptor getNoLoginInterceptor() {
        return  new NoLoginInterceptor();
    }

    /**
     * 设置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getNoLoginInterceptor()) // 添加指定的拦截器
                .addPathPatterns("/**") // 设置拦截的资源路径
                // 设置不需要拦截的资源路径
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
