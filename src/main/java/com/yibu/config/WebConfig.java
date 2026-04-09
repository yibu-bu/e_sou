package com.yibu.config;

import com.yibu.aop.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // 拦截所有接口，（注意不是/api/**这个路径，MVC拦截器的路径是剥离上下文路径之后的路径，而不是原始路径）
                .addPathPatterns("/**")
                // 排除登录、注册等公开接口
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/test")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-resources")
                .excludePathPatterns("/v3/api-docs")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/error");
    }
}