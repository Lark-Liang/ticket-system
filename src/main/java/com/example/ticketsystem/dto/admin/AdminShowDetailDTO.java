package com.example.ticketsystem.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lark
 * @ date 2026/3/2  22:02
 * @ description 管理员演出详情DTO
 */
@Data
public class AdminShowDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String city;
    private String venue;
    private String coverImage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double minPrice;
    private Double maxPrice;
    private Integer status;           // 1-上架 0-下架
    private Integer totalStock;       // 总库存
    private Integer availableStock;   // 可用库存
    private LocalDateTime saleStartTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 票档列表
    private List<AdminTicketTierDTO> ticketTiers;
}
