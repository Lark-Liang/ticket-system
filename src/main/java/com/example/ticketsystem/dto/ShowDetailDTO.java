package com.example.ticketsystem.dto;

import com.example.ticketsystem.entity.Show;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/2/28  17:54
 * @ description 演出详情DTO
 */
@Data
public class ShowDetailDTO {
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
    private Integer status;
    private Boolean onSale;
    private Boolean hasStock;

    // 票档列表
    private List<Map<String, Object>> ticketTiers;

    public static ShowDetailDTO fromShow(Show show) {
        if (show == null) return null;

        ShowDetailDTO dto = new ShowDetailDTO();
        dto.setId(show.getId());
        dto.setTitle(show.getTitle());
        dto.setDescription(show.getDescription());
        dto.setCategory(show.getCategory());
        dto.setCity(show.getCity());
        dto.setVenue(show.getVenue());
        dto.setCoverImage(show.getCoverImage());
        dto.setStartTime(show.getStartTime());
        dto.setEndTime(show.getEndTime());
        dto.setMinPrice(show.getMinPrice());
        dto.setMaxPrice(show.getMaxPrice());
        dto.setStatus(show.getStatus());
        dto.setOnSale(show.isOnSale());
        dto.setHasStock(show.hasStock());

        return dto;
    }
}
