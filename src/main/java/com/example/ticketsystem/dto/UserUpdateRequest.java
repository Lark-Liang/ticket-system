package com.example.ticketsystem.dto;

import java.time.LocalDate;

/**
 * @author Lark
 * @ date 2025/12/12  17:30
 * @ description
 */
public class UserUpdateRequest {
    private String nickname;
    private String bio;
    private Integer gender;
    private LocalDate birthday;
    private String backgroundImage;
    private String phone;
    private String email;
    private String avatar;

    //Getter和Setter
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
