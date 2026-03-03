package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  22:05
 * @ description 管理员票档请求DTO
 */
@Data
public class AdminTicketTierRequestDTO {
    private String name;
    private String description;
    private Double price;
    private Integer totalStock;
    private Integer status;  // 1-上架 0-下架
}
