package com.example.ticketsystem.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2025/12/19  20:31
 * @ description 邮箱登录请求DTO
 */
public class EmailLoginRequest {
    private String email;   //邮箱地址
    private String password;   //密码

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
