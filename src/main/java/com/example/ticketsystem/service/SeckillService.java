package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.SeckillRequest;

/**
 * @author Lark
 * @ date 2025/12/18  20:23
 * @ description 抢票服务接口
 */
public interface SeckillService {
    //抢票核心逻辑
    String seckillTicket(Long userId, Long showId, SeckillRequest request);
}
