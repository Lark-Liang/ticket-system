package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  22:26
 * @ description 管理员修改订单状态请求DTO
 */
@Data
public class AdminOrderStatusUpdateDTO {
    private String status;
}
