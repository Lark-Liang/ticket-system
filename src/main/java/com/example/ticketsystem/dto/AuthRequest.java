package com.example.ticketsystem.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2025/12/11  20:46
 * @ description 登录Request
 */
@Data
public class AuthRequest {
    private String username;   // 用户名（必填）
    private String password;   // 密码（必填）
    private String email;   // 邮箱（注册时可选）
    private String nickname;   // 昵称（注册时可选）
    private String phone;   // 手机号（注册时可选）

    //简单验证方法
    public boolean isValidForRegister() {
        return username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty();
    }

    public boolean isValidForLogin() {
        return username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty();
    }
}
