package com.example.ticketsystem.entity;

import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2025/12/12  20:07
 * @ description 演出实体
 */
public class Show {
    private Long id;
    private String title;   //演出标题
    private String description;   //描述
    private String category;   //分类：concert(演唱会)、drama(话剧)、music(音乐会)、exhibition(展览)
    private String city;   //城市
    private String venue;   //场馆
    private String coverImage;   //封面图URL
    private LocalDateTime startTime;   //开始时间
    private LocalDateTime endTime;   //结束时间
    private Double minPrice;   //最低价
    private Double maxPrice;   //最高价
    private Integer status;   //状态：1-上架 0-下架
    private Integer totalStock;   //总库存
    private Integer availableStock;   //可用库存
    private LocalDateTime saleStartTime;   //开售时间
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ============ Getter/Setter ============
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getTotalStock() { return totalStock; }
    public void setTotalStock(Integer totalStock) { this.totalStock = totalStock; }

    public Integer getAvailableStock() { return availableStock; }
    public void setAvailableStock(Integer availableStock) { this.availableStock = availableStock; }

    public LocalDateTime getSaleStartTime() { return saleStartTime; }
    public void setSaleStartTime(LocalDateTime saleStartTime) { this.saleStartTime = saleStartTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    //============ 业务方法 ============
    //判断是否已开票
    public boolean isOnSale() {
        return saleStartTime != null && LocalDateTime.now().isAfter(saleStartTime);
    }

    //判断是否有库存（用户端用）
    public boolean hasStock() {
        return availableStock != null && availableStock > 0;
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", city='" + city + '\'' +
                ", availableStock=" + availableStock +
                '}';
    }
}
