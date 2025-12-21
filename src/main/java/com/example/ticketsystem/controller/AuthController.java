package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.AuthRequest;
import com.example.ticketsystem.dto.EmailLoginRequest;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.service.AuthService;
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

    /**登录/注册接口
     * POST /auth/login
     * Body (application/json)
     * - username: 用户名(必填)
     * - password: 密码(必填)
     * - email: 邮箱(注册时可选)
     * - nickname: 昵称(注册时可选)
     * - phone: 手机号(注册时可选)
     */
    @PostMapping("/login")
    public ApiResponse<?> auth(@RequestBody AuthRequest request) {
        try {
            //基本验证
            if (!request.isValidForLogin()) {
                return ApiResponse.error(400, "用户名和密码不能为空");
            }

            //调用统一的认证服务
            User user = authService.auth(request);

            //生成Token
            Map<String, Object> data = new HashMap<>();
            String token = "user_" + user.getId() + "_" + System.currentTimeMillis();

            //构建响应数据
            data.put("token", token);
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("nickname", user.getNickname());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());

            //判断是登录还是注册
            boolean isNewUser = false;
            if (user.getCreatedAt() != null) {
                //如果创建时间与当前时间相差很小，认为是新用户
                long createTimeMillis = user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long currentTimeMillis = System.currentTimeMillis();
                isNewUser = (currentTimeMillis - createTimeMillis) < 10000;   //10秒内
            }

            String message = isNewUser ? "注册并登录成功" : "登录成功";
            data.put("isNewUser", isNewUser);   //告诉前端是否是注册

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
