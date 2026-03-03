package com.example.ticketsystem.dto.admin;

import com.example.ticketsystem.entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/3/2  18:30
 * @ description 管理员用户详情DTO
 */
@Data
public class AdminUserDetailDTO {
    private Long id;
    private String username;
    private String nickname;
    private String bio;
    private Integer gender;      // 0-未知，1-女，2-男
    private LocalDate birthday;
    private String backgroundImage;
    private String phone;
    private String email;
    private String avatar;
    private String role;
    private Integer status;       // 1-正常，0-禁用
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AdminUserDetailDTO fromUser(User user) {
        if (user == null) return null;

        AdminUserDetailDTO dto = new AdminUserDetailDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setBio(user.getBio());
        dto.setGender(user.getGender());
        dto.setBirthday(user.getBirthday());
        dto.setBackgroundImage(user.getBackgroundImage());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}
