package com.example.ticketsystem.dto.admin;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/3/2  22:06
 * @ description 管理员演出查询参数DTO
 */
@Data
public class AdminShowQueryDTO {
    private String title;        // 演出名称（模糊查询）
    private String city;         // 城市
    private String category;     // 分类
    private Integer status;      // 状态：1-上架 0-下架
    private String startDate;    // 开始日期范围
    private String endDate;      // 结束日期范围
    private Integer page = 1;
    private Integer size = 10;
}
