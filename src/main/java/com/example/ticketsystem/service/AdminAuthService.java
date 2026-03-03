package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.TokenResponse;
import com.example.ticketsystem.dto.admin.AdminLoginRequest;

/**
 * @author Lark
 * @ date 2026/3/2  18:14
 * @ description 管理员认证服务接口
 */
public interface AdminAuthService {
    //管理员登录
    TokenResponse login(AdminLoginRequest request);

    //管理员退出
    void logout(Long adminId);
}
