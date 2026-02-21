package com.example.ticketsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2025/12/18  19:39
 * @ description 订单实体
 */
@Data
public class Order {
    private Long id;
    private String orderNo;   //订单号（唯一）
    private Long userId;   //用户ID
    private Long showId;   //演出ID
    private Long sessionId;   //场次ID
    private Long ticketTierId;   //票档ID
    private Integer quantity;   //购买数量
    private Double unitPrice;   //单价
    private Double totalAmount;   //总金额
    private String status;   //订单状态：待支付pending/已支付paid/已取消cancelled
    private Long addressId;   //收货地址ID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
