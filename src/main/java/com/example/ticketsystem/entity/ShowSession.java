package com.example.ticketsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Lark
 * @ date 2025/12/17  21:14
 * @ description 演出场次实体类
 */
@Data
public class ShowSession {
    private Long id;
    private Long showId;   //演出ID
    private LocalDateTime sessionTime;   //场次时间
    private Integer status;   //1-可售 0-不可售
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
