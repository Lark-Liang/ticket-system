package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.LoginRequest;
import com.example.ticketsystem.dto.EmailLoginRequest;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/11  20:55
 * @ description：认证模块（用户名/邮箱登录接口，退出接口等），负责身份验证
 */

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**用户名登录接口
     * POST /auth/login
     * Body (application/json)
     * {
     *     "username":"string",
     *     "password":"string"
     * }
     */
    @PostMapping("/login")
    public ApiResponse<Object> login(@RequestBody LoginRequest request) {
        //调用Service登录
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ApiResponse.error(401, "用户名或密码错误");
        }
        //检查用户状态
        // TODO：管理端还要完善启用/禁用用户的相关代码
        if (user.getStatus() != null && user.getStatus() == 0) {
            return ApiResponse.error(403, "用户已被禁用");
        }
        //返回token
        // TODO：先用这个假token，后续再改用JWT升级
        String token = "user_" + user.getId() + "_" + System.currentTimeMillis();
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("nickname", user.getNickname());
        return ApiResponse.success("登录成功", data);
    }

    /**
     * 邮箱登录接口
     * POST /auth/login/email
     * Body:
     * - email: 邮箱地址(必填)
     * - password: 密码(必填)
     */
    @PostMapping("/login/email")
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

        //生成Token
        Map<String, Object> data = new HashMap<>();
        String token = "user_" + user.getId() + "_" + System.currentTimeMillis();

        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("nickname", user.getNickname());
        data.put("role", user.getRole());

        return ApiResponse.success("登录成功", data);
    }

    /**退出接口
     * POST /auth/logout
     * Headers:
     *  - Authorization: Bearer{token}
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout(@RequestHeader("Authorization") String authHeader) {
        //验证Token格式
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ApiResponse.error(401, "未授权");
        }

        // TODO: 后续这里应该：将Token加入黑名单（Redis）、清除用户会话、记录日志

        return ApiResponse.success("退出成功");
    }
}
