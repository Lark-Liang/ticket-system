package com.example.ticketsystem.controller;

import com.example.ticketsystem.annotation.PassToken;
import com.example.ticketsystem.config.JwtProperties;
import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.util.EnvUtil;
import com.example.ticketsystem.util.JwtUtil;
import com.example.ticketsystem.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/25  21:08
 * @ description 测试Controller，验证JWT配置是否生效
 */
@RestController
@RequestMapping("/api/config")
public class ConfigController {
    // TODO：这些东西不应该出现在controller里，参数配置要放在config里作为@component，测试要放在test文件夹
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenUtil tokenUtil;

    @Value("${spring.application.name:ticket-system}")
    private String appName;

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * 获取JWT配置信息
     * GET /api/config/jwt
     */
    @GetMapping("/jwt")
    public ApiResponse<?> getJwtConfig(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        Map<String, Object> config = new HashMap<>();
        config.put("application", appName);
        config.put("environment", envUtil.getActiveProfile());
        config.put("isProduction", envUtil.isProduction());
        config.put("serverPort", serverPort);
        config.put("currentUserId", userId);

        // JWT配置
        Map<String, Object> jwtConfig = new HashMap<>();
        jwtConfig.put("secretKeyLength", jwtProperties.getSecretKey().length());
        jwtConfig.put("accessTokenExpiration", jwtProperties.getAccessTokenExpiration());
        jwtConfig.put("refreshTokenExpiration", jwtProperties.getRefreshTokenExpiration());
        jwtConfig.put("tokenPrefix", jwtProperties.getTokenPrefix());
        jwtConfig.put("isValid", jwtProperties.isValid());
        jwtConfig.put("securityWarning", jwtProperties.getSecurityWarning());

        config.put("jwt", jwtConfig);

        return ApiResponse.success("JWT配置信息", config);
    }

    /**
     * 测试JWT生成和解析
     */
    @GetMapping("/jwt/test")
    @PassToken
    public ApiResponse<?> testJwtGeneration() {
        Long testUserId = 999L;
        String testUsername = "testuser";
        String testRole = "user";

        // 生成Token
        String accessToken = jwtUtil.generateAccessToken(testUserId, testUsername, testRole);
        String refreshToken = jwtUtil.generateRefreshToken(testUserId, testUsername);

        // 验证Token
        boolean accessTokenValid = jwtUtil.validateToken(accessToken);
        boolean refreshTokenValid = jwtUtil.validateToken(refreshToken);

        // 解析Token
        Long parsedUserId = jwtUtil.getUserIdFromToken(accessToken);
        String parsedUsername = jwtUtil.getUsernameFromToken(accessToken);
        String parsedRole = jwtUtil.getRoleFromToken(accessToken);

        long remainingTime = jwtUtil.getRemainingTime(accessToken);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("accessTokenLength", accessToken.length());
        result.put("refreshToken", refreshToken);
        result.put("refreshTokenLength", refreshToken.length());
        result.put("accessTokenValid", accessTokenValid);
        result.put("refreshTokenValid", refreshTokenValid);
        result.put("parsedUserId", parsedUserId);
        result.put("parsedUsername", parsedUsername);
        result.put("parsedRole", parsedRole);
        result.put("remainingTimeSeconds", remainingTime);
        result.put("isMatch", testUserId.equals(parsedUserId) &&
                testUsername.equals(parsedUsername) &&
                testRole.equals(parsedRole));

        return ApiResponse.success("JWT功能测试", result);
    }

    /**
     * 测试Token工具类
     */
    @GetMapping("/token/test")
    @PassToken
    public ApiResponse<?> testTokenUtil(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> result = new HashMap<>();

        // 如果没有提供Token，生成一个测试
        if (authHeader == null) {
            String testToken = jwtUtil.generateAccessToken(888L, "tester", "admin");
            authHeader = "Bearer " + testToken;
            result.put("generatedTestToken", testToken);
        }

        result.put("authHeader", authHeader);
        result.put("tokenValid", jwtUtil.validateToken(authHeader.substring(7)));

        // 测试TokenUtil
        result.put("extractedUserId", tokenUtil.extractUserIdFromToken(authHeader));
        result.put("extractedUsername", tokenUtil.getUsernameFromToken(authHeader));
        result.put("extractedRole", tokenUtil.getRoleFromToken(authHeader));
        result.put("tokenUtilValid", tokenUtil.validateToken(authHeader));

        return ApiResponse.success("Token工具测试", result);
    }
}