package com.example.ticketsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2025/12/17  21:18
 * @ description 票档实体
 */
@Data
public class TicketTier {
    private Long id;
    private Long showId;   //演出ID
    private String name;   //VIP票/普通票/学生票
    private String description;
    private Double price;
    private Integer totalStock;   //总库存
    private Integer availableStock;   //可用库存
    private Integer status;   //1-上架 0-下架
    private Integer version;   //乐观锁版本号
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
