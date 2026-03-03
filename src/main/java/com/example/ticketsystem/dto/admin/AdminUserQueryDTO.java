package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  18:32
 * @ description 管理员用户查询参数DTO
 */
@Data
public class AdminUserQueryDTO {
    private String username;      // 用户名
    private String phone;         // 手机号
    private String email;         // 邮箱
    private Integer status;       // 状态：1-正常，0-禁用
    private String role;          // 角色：user/admin
    private String startDate;     // 注册开始日期
    private String endDate;       // 注册结束日期
    private Integer page = 1;
    private Integer size = 10;
}
