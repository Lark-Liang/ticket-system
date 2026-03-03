package com.example.ticketsystem.dto.admin;

import com.example.ticketsystem.entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/3/2  18:28
 * @ description 管理员用户列表返回DTO
 */
@Data
public class AdminUserListDTO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String role;
    private Integer status;      // 1-正常，0-禁用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AdminUserListDTO fromUser(User user) {
        if (user == null) return null;

        AdminUserListDTO dto = new AdminUserListDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}
