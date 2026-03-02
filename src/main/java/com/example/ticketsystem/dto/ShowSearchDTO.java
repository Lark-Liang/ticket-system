package com.example.ticketsystem.dto;

import com.example.ticketsystem.entity.Show;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2026/2/28  21:11
 * @ description 演出搜索结果DTO
 */
@Data
public class ShowSearchDTO {
    private Long id;
    private String title;
    private String category;
    private String city;
    private String coverImage;
    private LocalDateTime startTime;
    private Boolean hasStock;
    private Boolean onSale;

    public static ShowSearchDTO fromShow(Show show) {
        if (show == null) return null;

        ShowSearchDTO dto = new ShowSearchDTO();
        dto.setId(show.getId());
        dto.setTitle(show.getTitle());
        dto.setCategory(show.getCategory());
        dto.setCity(show.getCity());
        dto.setCoverImage(show.getCoverImage());
        dto.setStartTime(show.getStartTime());
        dto.setHasStock(show.hasStock());
        dto.setOnSale(show.isOnSale());

        return dto;
    }
}
