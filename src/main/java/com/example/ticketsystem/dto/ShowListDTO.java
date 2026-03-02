package com.example.ticketsystem.dto;

import com.example.ticketsystem.entity.Show;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/2/28  21:10
 * @ description 演出列表DTO
 */
@Data
public class ShowListDTO {
    private Long id;
    private String title;
    private String category;
    private String city;
    private String coverImage;
    private String venue;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double minPrice;
    private Double maxPrice;
    private Boolean hasStock;
    private Boolean onSale;

    public static ShowListDTO fromShow(Show show) {
        if (show == null) return null;

        ShowListDTO dto = new ShowListDTO();
        dto.setId(show.getId());
        dto.setTitle(show.getTitle());
        dto.setCategory(show.getCategory());
        dto.setCity(show.getCity());
        dto.setCoverImage(show.getCoverImage());
        dto.setVenue(show.getVenue());
        dto.setStartTime(show.getStartTime());
        dto.setEndTime(show.getEndTime());
        dto.setMinPrice(show.getMinPrice());
        dto.setMaxPrice(show.getMaxPrice());
        dto.setHasStock(show.hasStock());
        dto.setOnSale(show.isOnSale());

        return dto;
    }
}
