package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  18:11
 * @ description 管理员登录请求DTO
 */
@Data
public class AdminLoginRequest {
    private String username;
    private String password;
}
