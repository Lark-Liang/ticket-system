package com.example.ticketsystem.util;

import com.example.ticketsystem.config.JwtProperties;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2025/12/23  20:42
 * @ description 统一Token工具类（替换各Controller中的重复代码）
 */
@Component
public class TokenUtil {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    //从Authorization Header中提取并验证Token，返回用户ID，如果无效则返回null
    // TODO:工具类的方法是静态方法，直接调用
    public Long extractUserIdFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null || !jwtUtil.validateToken(token)) {
            return null;
        }
        return jwtUtil.getUserIdFromToken(token);
    }

    //从Authorization Header中提取Token字符串
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(jwtProperties.getTokenPrefix())) {
            return null;
        }
        return authHeader.substring(jwtProperties.getTokenPrefix().length()).trim();
    }

    //验证Token是否有效
    public boolean validateToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        return token != null && jwtUtil.validateToken(token);
    }

    //从Token中获取用户名
    public String getUsernameFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) return null;
        return jwtUtil.getUsernameFromToken(token);
    }

    //从Token中获取用户角色
    public String getRoleFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) return null;
        return jwtUtil.getRoleFromToken(token);
    }

    //生成带前缀的完整Token字符串（用于响应）
    public String buildAuthHeader(String token) {
        return jwtProperties.getTokenPrefix() + token;
    }

    //从Token中获取Claims
    public Claims getClaimsFromToken(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) return null;
        return jwtUtil.getClaimsFromToken(token);
    }
}

