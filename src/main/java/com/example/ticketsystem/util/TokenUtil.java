package com.example.ticketsystem.util;

import com.example.ticketsystem.config.JwtProperties;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2025/12/23  20:42
 * @ description 统一Token工具类（替换各Controller中的重复代码）
 */
@Component
public class TokenUtil {
    private static JwtUtil jwtUtil;
    private static JwtProperties jwtProperties;

    @Autowired
    private JwtUtil jwtUtilInstance;
    @Autowired
    private JwtProperties jwtPropertiesInstance;

    @PostConstruct
    private void init() {
        TokenUtil.jwtUtil = this.jwtUtilInstance;
        TokenUtil.jwtProperties = this.jwtPropertiesInstance;
        System.out.println("=== TokenUtil 初始化完成 ===");  // 可以看看有没有打印
    }

    //从Authorization Header中提取并验证Token，返回用户ID，如果无效则返回null
    public static Long extractUserIdFromToken(String authHeader) {
        System.out.println("jwtUtil is null? " + (jwtUtil == null));
        System.out.println("jwtProperties is null? " + (jwtProperties == null));

        //null检查
        if (jwtUtil == null || jwtProperties == null) {
            throw new RuntimeException("TokenUtil 未正确初始化");
        }

        String token = extractTokenFromHeader(authHeader);
        if (token == null || !jwtUtil.validateToken(token)) {
            return null;
        }
        return jwtUtil.getUserIdFromToken(token);
    }

    //从Authorization Header中提取Token字符串
    public static String extractTokenFromHeader(String authHeader) {
        if (jwtProperties == null) {
            throw new RuntimeException("TokenUtil 未正确初始化");
        }
        if (authHeader == null || !authHeader.startsWith(jwtProperties.getTokenPrefix())) {
            return null;
        }
        return authHeader.substring(jwtProperties.getTokenPrefix().length()).trim();
    }

    //验证Token是否有效
    public static boolean validateToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        return token != null && jwtUtil.validateToken(token);
    }

    //从Token中获取用户名
    public static String getUsernameFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) return null;
        return jwtUtil.getUsernameFromToken(token);
    }

    //从Token中获取用户角色
    public static String getRoleFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) return null;
        return jwtUtil.getRoleFromToken(token);
    }

    //生成带前缀的完整Token字符串（用于响应）
    public static String buildAuthHeader(String token) {
        return jwtProperties.getTokenPrefix() + token;
    }

    //从Token中获取Claims
    public static Claims getClaimsFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) return null;
        return jwtUtil.getClaimsFromToken(token);
    }
}

