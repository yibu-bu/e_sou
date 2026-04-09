package com.yibu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许携带 Cookie
                .allowCredentials(true)
                // 允许访问的域名，如果要允许所有域名访问，必须用 allowedOriginPatterns 这个方法，
                // 否则 * 会和 allowCredentials 冲突。（规范来讲允许携带凭证时不应该允许所有源访问）
                .allowedOriginPatterns("*")
                // 允许的请求方法
                .allowedMethods("*")
                // 允许的请求头
                .allowedHeaders("*")
                // 允许浏览器访问所有响应头
                .exposedHeaders("*")
                .maxAge(3600);
    }
}