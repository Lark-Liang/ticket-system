package com.example.ticketsystem.config;

import com.example.ticketsystem.annotation.PassToken;
import com.example.ticketsystem.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Lark
 * @ date 2026/2/19  16:24
 * @ description 拦截器(统一处理Token验证)
 */
@Component
public class AuthInterceptor implements  HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查方法是否有 @PassToken 注解
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;  // 跳过验证
            }
        }

        // 检查类是否有 @PassToken 注解（类上的注解对该类所有方法生效）
        Class<?> controllerClass = handlerMethod.getBeanType();
        if (controllerClass.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = controllerClass.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;  // 跳过验证
            }
        }

        // 从请求头获取 Token
        String authHeader = request.getHeader("Authorization");

        // 提取并验证 Token
        Long userId = TokenUtil.extractUserIdFromToken(authHeader);
        if (userId == null) {
            // 验证失败，返回401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权\",\"data\":null}");
            return false;
        }

        // 将 userId 存入 request，方便 Controller 使用
        request.setAttribute("userId", userId);

        return true;
    }
}
