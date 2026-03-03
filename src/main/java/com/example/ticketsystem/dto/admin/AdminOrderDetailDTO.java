package com.example.ticketsystem.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/3/2  22:24
 * @ description 管理员订单详情DTO
 */
@Data
public class AdminOrderDetailDTO {
    private Long id;
    private String orderNo;

    // 用户信息
    private Long userId;
    private String username;
    private String userPhone;
    private String userEmail;

    // 演出信息
    private Long showId;
    private String showTitle;
    private String showCover;
    private String showVenue;
    private String showCity;
    private LocalDateTime showStartTime;
    private LocalDateTime showEndTime;

    // 场次信息
    private Long sessionId;
    private LocalDateTime sessionTime;

    // 票档信息
    private Long ticketTierId;
    private String ticketTierName;
    private Double unitPrice;

    // 订单信息
    private Integer quantity;
    private Double totalAmount;
    private String status;

    // 地址信息
    private Long addressId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
