package com.example.ticketsystem.controller.admin;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.TokenResponse;
import com.example.ticketsystem.dto.admin.AdminLoginRequest;
import com.example.ticketsystem.service.AdminAuthService;
import com.example.ticketsystem.util.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/3/2  18:18
 * @ description 管理员认证控制器
 */
@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {
    @Autowired
    private AdminAuthService adminAuthService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AdminLoginRequest request) {
        try {
            TokenResponse tokenResponse = adminAuthService.login(request);
            return ApiResponse.success("管理员登录成功", tokenResponse);
        } catch (RuntimeException e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }

    /**
     * 管理员退出
     */
    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        Long adminId = RequestHolder.getUserId();
        adminAuthService.logout(adminId);
        return ApiResponse.success("退出成功");
    }
}
