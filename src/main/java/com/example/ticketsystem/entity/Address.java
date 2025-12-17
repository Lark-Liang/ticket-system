package com.example.ticketsystem.entity;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2025/12/11  20:45
 * @ description
 */
public class Address {
    private Long id;
    private Long userId;
    private String name;
    private String phone;
    private String address;
    private Integer isDefault;  // 1-默认，0-非默认
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime updatedAt= LocalDateTime.now();

    //空参构造
    public Address() {}

    // Getter和Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getIsDefault() { return isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // toString（调试用）
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
