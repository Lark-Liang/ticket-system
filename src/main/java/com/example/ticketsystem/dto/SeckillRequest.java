package com.example.ticketsystem.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2025/12/18  20:04
 * @ description 抢票请求DTO
 */
@Data
public class SeckillRequest {
    private Long sessionId;   // 场次ID
    private Long ticketTierId;   // 票档ID
    private Integer quantity;   // 购买数量（默认为1）
}
