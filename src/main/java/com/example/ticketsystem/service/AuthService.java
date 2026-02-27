package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.AuthRequest;
import com.example.ticketsystem.dto.TokenResponse;
import com.example.ticketsystem.entity.User;

/**
 * @author Lark
 * @ date 2025/12/11  20:53
 * @ description：认证模块，（负责处理用户登录验证等）
 */

public interface AuthService {
    //登录/注册二合一
    TokenResponse auth(AuthRequest request);

    //登录验证
    User login(User user, String password);

    //邮箱登录，返回TokenResponse
    TokenResponse loginByEmail(String email, String password);
}
