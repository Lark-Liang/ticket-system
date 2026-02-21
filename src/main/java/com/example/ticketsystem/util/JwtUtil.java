package com.example.ticketsystem.util;

import com.example.ticketsystem.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/23  20:10
 * @ description JWT工具类
 */
@Component
public class JwtUtil {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EnvUtil envUtil;

    //生成Access Token
    public String generateAccessToken(Long userId, String username, String role) {
        return generateToken(userId, username, role, jwtProperties.getAccessTokenExpiration());
    }

    //生成Refresh Token
    public String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return generateToken(userId, username, null, jwtProperties.getRefreshTokenExpiration(), claims);
    }

    //生成Token（基础方法）
    private String generateToken(Long userId, String username, String role, long expiration) {
        return generateToken(userId, username, role, expiration, new HashMap<>());
    }

    private String generateToken(Long userId, String username, String role, long expiration, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        claims.put("userId", userId);
        claims.put("username", username);
        if (role != null) {
            claims.put("role", role);
        }
        claims.put("type", "access");
        claims.put("env", envUtil.getActiveProfile()); //添加环境标记

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, getSecretKeyString())
                .compact();
    }

    //从Token中解析Claims
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("JWT解析失败: " + e.getMessage(), e);
        }
    }

    //从Token中获取用户ID
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get("userId");
        //处理可能的类型转换
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        } else if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }

    //从Token中获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("username");
    }

    //从Token中获取用户角色
    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("role");
    }

    //验证Token是否有效
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //检查Token是否即将过期（在指定时间内）
    public boolean isTokenExpiringSoon(String token, int minutes) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
            return timeUntilExpiration <= minutes * 60 * 1000;
        } catch (Exception e) {
            return false;
        }
    }

    //获取Token剩余时间（秒）
    public long getRemainingTime(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            long remainingMillis = expiration.getTime() - System.currentTimeMillis();
            return Math.max(0, remainingMillis / 1000);
        } catch (Exception e) {
            return 0;
        }
    }

    //获取Token的Claims
    public Claims getClaimsFromToken(String token) {
        return parseToken(token);
    }

    //获取密钥
    private String getSecretKeyString() {
        //优先使用EnvUtil获取密钥
        String secret = envUtil.getJwtSecret();
        if (secret == null || secret.trim().isEmpty()) {
            //回退到配置类
            secret = jwtProperties.getSecretKey();
        }

        //确保密钥长度足够
        if (secret.length() < 32) {
            if (envUtil.isDevelopment()) {
                //开发环境：自动补全密钥
                secret = String.format("%-32s", secret).replace(' ', 'X');
            }
        }

        return secret;
    }
}
