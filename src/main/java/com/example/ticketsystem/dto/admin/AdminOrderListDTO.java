package com.example.ticketsystem.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/3/2  22:22
 * @ description 管理员订单列表返回DTO
 */
@Data
public class AdminOrderListDTO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private Long showId;
    private String showTitle;
    private String showCover;
    private LocalDateTime sessionTime;
    private String ticketTierName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private String status;          // pending/paid/cancelled
    private LocalDateTime createdAt;
}
