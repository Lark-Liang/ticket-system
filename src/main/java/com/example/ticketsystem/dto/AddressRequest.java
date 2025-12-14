package com.example.ticketsystem.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2025/12/12  16:36
 * @ description
 */
@Data
public class AddressRequest {
    private String name;
    private String phone;
    private String address;
    private Integer isDefault;  // 1-设置默认，0-不设置
}
