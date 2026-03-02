package com.example.ticketsystem.dto;

import com.example.ticketsystem.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/2/28  21:13
 * @ description 订单DTO
 */
@Data
public class OrderDTO {
    private Long id;
    private String orderNo;
    private Long showId;
    private String showTitle;      // 演出名称（关联查询）
    private String showCover;       // 演出封面
    private Long sessionId;
    private LocalDateTime sessionTime;  // 场次时间
    private String ticketTierName;  // 票档名称
    private Integer quantity;
    private Double unitPrice;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public static OrderDTO fromOrder(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setShowId(order.getShowId());
        dto.setSessionId(order.getSessionId());
        dto.setTicketTierName("待补充");  // 需要关联查询
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        return dto;
    }
}
