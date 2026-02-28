package com.example.ticketsystem.controller;

import com.example.ticketsystem.config.JwtProperties;
import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.util.EnvUtil;
import com.example.ticketsystem.util.JwtUtil;
import com.example.ticketsystem.util.RequestHolder;
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
 * @ description 配置查看接口
 */
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private EnvUtil envUtil;

    @Value("${spring.application.name:ticket-system}")
    private String appName;

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * 获取JWT配置信息
     */
    @GetMapping("/jwt")
    public ApiResponse<?> getJwtConfig() {
        Long userId = RequestHolder.getUserId();

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
}