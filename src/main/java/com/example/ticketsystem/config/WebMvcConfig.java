package com.example.ticketsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lark
 * @ date 2026/2/19  16:27
 * @ description Web MVC 配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",              // 登录
                        "/auth/login/email",         // 邮箱登录
                        "/auth/refresh",              // 刷新Token
                        "/shows/home",                 // 首页演出
                        "/shows/search",                // 搜索
                        "/shows/list",                   // 演出列表
                        "/shows/cities",                  // 城市列表
                        "/shows/categories",              // 分类列表
                        "/shows/*",                        // 演出详情
                        "/api/config/jwt/test",            // JWT生成测试
                        "/api/config/token/test",          // Token工具测试
                        "/error"                           // 错误页面
                );
    }
}
