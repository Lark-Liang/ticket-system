package com.example.ticketsystem.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2025/12/23  20:05
 * @ description JWT配置类
 */
/**
 * JWT配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * 密钥：优先从环境变量JWT_SECERT读取，如果没有则使用默认值
     * 生产环境必须通过环境变量设置
     */
    @Value("${jwt.secret-key}")
    private String secretKey;

    //Access Token过期时间（秒）
    private long accessTokenExpiration = 7200;   //2小时

    //Refresh Token过期时间（秒）
    private long refreshTokenExpiration = 604800;   //7天

    //Token前缀
    private String tokenPrefix = "Bearer ";

    //Token在Header中的Key
    private String tokenHeader = "Authorization";

    //验证配置是否有效
    public boolean isValid() {
        return secretKey != null && !secretKey.trim().isEmpty()
                && secretKey.length() >= 32;
    }

    //获取安全警告信息（如果配置不安全）
    public String getSecurityWarning() {
        if (secretKey.contains("must-be-32-chars-long") ||
                secretKey.contains("please-change")) {
            return "警告：正在使用默认密钥，生产环境不安全！";
        }
        if (secretKey.length() < 32) {
            return "警告：密钥长度不足32位，建议使用更长的密钥！";
        }
        return null;
    }
}
