package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  18:31
 * @ description 管理员修改用户信息请求DTO
 */
@Data
public class AdminUserUpdateRequest {
    private String nickname;
    private String bio;
    private Integer gender;
    private String birthday;
    private String backgroundImage;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;        // 1-正常，0-禁用
}
