package com.example.ticketsystem.controller;

import com.example.ticketsystem.annotation.PassToken;
import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.AuthRequest;
import com.example.ticketsystem.dto.EmailLoginRequest;
import com.example.ticketsystem.dto.TokenResponse;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.service.AuthService;
import com.example.ticketsystem.util.JwtUtil;
import com.example.ticketsystem.util.TokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/11  20:55
 * @ description 认证模块（用户名/邮箱登录接口，退出接口等），负责身份验证
 */

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    /**登录/注册接口
     */
    @PostMapping("/login")
    @PassToken
    public ApiResponse<?> auth(@RequestBody AuthRequest request) {
        try {
            //基本验证
            if (!request.isValidForLogin()) {
                return ApiResponse.error(400, "用户名和密码不能为空");
            }

            //调用统一的认证服务
            User user = authService.auth(request);

            //生成JWT Token
            String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

            //构建响应数据
            TokenResponse tokenResponse = new TokenResponse(
                    accessToken,
                    refreshToken,
                    jwtUtil.getRemainingTime(accessToken),
                    user.getId(),
                    user.getUsername(),
                    user.getRole()
            );

            //判断是登录还是注册
            boolean isNewUser = false;
            if (user.getCreatedAt() != null) {
                //如果创建时间与当前时间相差很小，认为是新用户
                long createTimeMillis = user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long currentTimeMillis = System.currentTimeMillis();
                isNewUser = (currentTimeMillis - createTimeMillis) < 10000;   //10秒内
            }

            Map<String, Object> data = new HashMap<>();
            data.put("token", tokenResponse);
            data.put("isNewUser", isNewUser);

            String message = isNewUser ? "注册并登录成功" : "登录成功";
            return ApiResponse.success(message, data);

        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(500, "系统错误，请稍后重试");
        }
    }

    /**
     * 邮箱登录接口
     */
    @PostMapping("/login/email")
    @PassToken
    public ApiResponse<?> loginByEmail(@RequestBody EmailLoginRequest request) {
        //参数验证
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ApiResponse.error(400, "邮箱不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ApiResponse.error(400, "密码不能为空");
        }

        //邮箱格式简单验证
        String email = request.getEmail().trim();
        if (!email.contains("@") || !email.contains(".")) {
            return ApiResponse.error(400, "邮箱格式不正确");
        }

        //调用Service进行登录验证
        User user = authService.loginByEmail(email, request.getPassword());
        if (user == null) {
            return ApiResponse.error(401, "邮箱或密码错误");
        }

        //检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            return ApiResponse.error(403, "用户已被禁用");
        }

        //生成JWT Token
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        TokenResponse tokenResponse = new TokenResponse(
                accessToken,
                refreshToken,
                jwtUtil.getRemainingTime(accessToken),
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        Map<String, Object> data = new HashMap<>();
        data.put("token", tokenResponse);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("nickname", user.getNickname());
        data.put("role", user.getRole());

        return ApiResponse.success("登录成功", data);
    }

    /**退出接口
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout(HttpServletRequest request)  {
        Long userId = (Long) request.getAttribute("userId");

        // TODO: 后续这里应该：将Token加入黑名单（Redis）、清除用户会话、记录日志
        System.out.println("用户退出: " + userId);

        return ApiResponse.success("退出成功");
    }

    /**
     * 刷新Access Token接口
     */
    @PostMapping("/refresh")
    @PassToken  //刷新Token接口，不需要验证（用旧的Refresh Token）
    public ApiResponse<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = TokenUtil.extractTokenFromHeader(authHeader);
            if (token == null) {
                return ApiResponse.error(401, "未提供Token");
            }

            //验证Token
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.error(401, "Token无效或已过期");
            }

            //获取Claims
            Claims claims = TokenUtil.getClaimsFromToken(authHeader);
            if (claims == null) {
                return ApiResponse.error(401, "无法解析Token");
            }

            //检查是否为Refresh Token
            String tokenType = (String) claims.get("type");
            if (!"refresh".equals(tokenType)) {
                return ApiResponse.error(400, "请使用Refresh Token");
            }

            //获取用户信息
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);

            if (userId == null || username == null) {
                return ApiResponse.error(400, "Token中缺少用户信息");
            }

            //生成新的Access Token（假设角色为user，可以根据需要从数据库查询）
            String newAccessToken = jwtUtil.generateAccessToken(userId, username, "user");

            TokenResponse tokenResponse = TokenResponse.of(
                    newAccessToken,
                    jwtUtil.getRemainingTime(newAccessToken),
                    userId,
                    username,
                    "user"
            );

            return ApiResponse.success("Token刷新成功", tokenResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(500, "Token刷新失败: " + e.getMessage());
        }
    }
}
