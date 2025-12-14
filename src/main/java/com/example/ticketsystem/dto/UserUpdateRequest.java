package com.example.ticketsystem.dto;

/**
 * @author Lark
 * @ date 2025/12/12  17:30
 * @ description
 */
public class UserUpdateRequest {
    private String nickname;
    private String phone;
    private String email;
    private String avatar;

    // Getter和Setter
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
