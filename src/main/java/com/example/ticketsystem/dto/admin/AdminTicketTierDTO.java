package com.example.ticketsystem.dto.admin;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @author Lark
 * @ date 2026/3/2  22:03
 * @ description 管理员票档DTO
 */
@Data
public class AdminTicketTierDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer totalStock;
    private Integer availableStock;
    private Integer status;  // 1-上架 0-下架
}
