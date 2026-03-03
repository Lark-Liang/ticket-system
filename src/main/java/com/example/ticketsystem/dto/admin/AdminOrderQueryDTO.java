package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  22:25
 * @ description 管理员订单查询参数DTO
 */
@Data
public class AdminOrderQueryDTO {
    private String orderNo;        // 订单号
    private Long userId;           // 用户ID
    private String username;       // 用户名
    private Long showId;           // 演出ID
    private String showTitle;      // 演出名称
    private String status;         // 订单状态：pending/paid/cancelled
    private String startDate;      // 下单开始日期
    private String endDate;        // 下单结束日期
    private Integer page = 1;
    private Integer size = 10;
}
