package com.example.ticketsystem.dto.admin;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lark
 * @ date 2026/3/2  22:04
 * @ description 管理员添加修改演出请求DTO
 */
@Data
public class AdminShowRequestDTO {
    // 演出基本信息
    private String title;
    private String description;
    private String category;
    private String city;
    private String venue;
    private String coverImage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;  // 1-上架 0-下架
    private LocalDateTime saleStartTime;

    // 票档列表
    private List<AdminTicketTierRequestDTO> ticketTiers;
}
