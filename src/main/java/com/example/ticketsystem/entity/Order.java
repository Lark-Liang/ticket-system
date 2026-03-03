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

    // ====== 关联查询字段 ======
    private String username;           // 用户名
    private String userPhone;          // 用户手机号
    private String userEmail;          // 用户邮箱
    private String showTitle;          // 演出名称
    private String showCover;          // 演出封面
    private String showVenue;          // 演出场馆
    private String showCity;           // 演出城市
    private LocalDateTime showStartTime; // 演出开始时间
    private LocalDateTime showEndTime;   // 演出结束时间
    private String ticketTierName;     // 票档名称
    private String receiverName;       // 收货人姓名
    private String receiverPhone;      // 收货人电话
    private String receiverAddress;    // 收货地址
    private LocalDateTime sessionTime;  // 场次时间
}
