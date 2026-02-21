package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.SeckillRequest;
import com.example.ticketsystem.entity.Show;

import java.util.List;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/2/13  20:55
 * @ description 演出服务接口
 */
public interface ShowService {
    // 首页演出列表
    List<Map<String, Object>> getHomeShows(String city, int limit);

    // 搜索演出
    List<Map<String, Object>> searchShows(String keyword);

    // 条件分页查询
    Map<String, Object> listShows(String city, String category, int page, int size);

    // 获取演出详情
    Map<String, Object> getShowDetail(Long showId);

    // 抢票
    String seckillTicket(Long userId, Long showId, SeckillRequest request);

    // 辅助接口
    List<String> getAllCities();
    List<String> getAllCategories();
}
