package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.LoginRequest;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/11  20:55
 * @ description：认证模块（登录接口，退出接口等），负责身份验证
 */

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**登录接口
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
        // TODO：（管理端还要完善启用/禁用用户的相关代码）
        if (user.getStatus() != null && user.getStatus() == 0) {
            return ApiResponse.error(403, "用户已被禁用");
        }
        //返回token
        // TODO：（先用这个假token，后续再改用JWT升级）
        String token = "user_" + user.getId() + "_" + System.currentTimeMillis();
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("nickname", user.getNickname());
        return ApiResponse.success("登录成功", data);
    }

    /**退出接口
     * POST /auth/logout
     * Headers:
     *  - Authorization: Bearer{token}
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout(@RequestHeader("Authorization") String authHeader) {
        return ApiResponse.success("退出成功");
    }

}
