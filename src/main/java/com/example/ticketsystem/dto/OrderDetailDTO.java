package com.example.ticketsystem.dto;

import com.example.ticketsystem.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/2/28  21:14
 * @ description 订单详情DTO
 */
@Data
public class OrderDetailDTO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;

    // 演出信息
    private Long showId;
    private String showTitle;
    private String showCover;
    private String showVenue;
    private String showCity;

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
    private Long addressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderDetailDTO fromOrder(Order order) {
        if (order == null) return null;

        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setUserId(order.getUserId());
        dto.setShowId(order.getShowId());
        dto.setSessionId(order.getSessionId());
        dto.setTicketTierId(order.getTicketTierId());
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setAddressId(order.getAddressId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }
}
