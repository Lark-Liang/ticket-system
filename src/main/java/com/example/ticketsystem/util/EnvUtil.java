package com.example.ticketsystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Lark
 * @ date 2025/12/25  20:41
 * @ description 环境变量工具类
 */
@Component
public class EnvUtil {
    @Autowired
    private Environment environment;

    //获取JWT密钥
    public String getJwtSecret() {
        //优先从系统环境变量获取
        String envSecret = System.getenv("JWT_SECRET");
        if (envSecret != null && !envSecret.trim().isEmpty()) {
            return envSecret;
        }

        //从Spring配置获取
        return environment.getProperty("jwt.secret-key");
    }

    //获取当前激活的profile
    public String getActiveProfile() {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length > 0) {
            return profiles[0];
        }
        return "default";
    }

    //判断是否为生产环境
    public boolean isProduction() {
        return getActiveProfile().equals("prod") ||
                getActiveProfile().equals("production");
    }

    //判断是否为开发环境
    public boolean isDevelopment() {
        return getActiveProfile().equals("dev") ||
                getActiveProfile().equals("development") ||
                getActiveProfile().equals("default");
    }
}
