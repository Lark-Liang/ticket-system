package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.SeckillRequest;
import com.example.ticketsystem.entity.Show;
import com.example.ticketsystem.mapper.ShowMapper;
import com.example.ticketsystem.service.SeckillService;
import com.example.ticketsystem.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lark
 * @ date 2026/2/13  21:08
 * @ description 演出服务实现类
 */
@Service
public class ShowServiceImpl implements ShowService{
    @Autowired
    private ShowMapper showMapper;

    @Autowired
    private SeckillService seckillService;  // 抢票服务

    @Override
    public List<Map<String, Object>> getHomeShows(String city, int limit) {
        // 参数处理
        if (city == null || city.trim().isEmpty()) {
            city = "北京";
        }
        if (limit <= 0) {
            limit = 10;
        }

        // 查询数据
        List<Show> shows = showMapper.findHomeShows(city, limit);

        // 构建返回格式（从Controller复制过来的逻辑）
        return shows.stream().map(show -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", show.getId());
            map.put("title", show.getTitle());
            map.put("category", show.getCategory());
            map.put("city", show.getCity());
            map.put("coverImage", show.getCoverImage());
            map.put("venue", show.getVenue());
            map.put("startTime", show.getStartTime());
            map.put("endTime", show.getEndTime());
            map.put("minPrice", show.getMinPrice());
            map.put("maxPrice", show.getMaxPrice());
            map.put("hasStock", show.hasStock());
            map.put("onSale", show.isOnSale());
            return map;
        }).toList();
    }

    @Override
    public List<Map<String, Object>> searchShows(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException("搜索关键词不能为空");
        }

        List<Show> shows = showMapper.searchByKeyword(keyword.trim());

        return shows.stream().map(show -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", show.getId());
            map.put("title", show.getTitle());
            map.put("category", show.getCategory());
            map.put("city", show.getCity());
            map.put("coverImage", show.getCoverImage());
            map.put("startTime", show.getStartTime());
            map.put("hasStock", show.hasStock());
            map.put("onSale", show.isOnSale());
            return map;
        }).toList();
    }

    @Override
    public Map<String, Object> listShows(String city, String category, int page, int size) {
        // 参数处理
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;

        int offset = (page - 1) * size;

        // 查询数据
        List<Show> shows = showMapper.findByConditions(city, category, offset, size);
        int total = showMapper.countByConditions(city, category);
        int totalPages = (int) Math.ceil((double) total / size);

        // 构建演出列表
        List<Map<String, Object>> showList = shows.stream().map(show -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", show.getId());
            map.put("title", show.getTitle());
            map.put("category", show.getCategory());
            map.put("city", show.getCity());
            map.put("coverImage", show.getCoverImage());
            map.put("startTime", show.getStartTime());
            map.put("minPrice", show.getMinPrice());
            map.put("maxPrice", show.getMaxPrice());
            map.put("hasStock", show.hasStock());
            map.put("onSale", show.isOnSale());
            return map;
        }).toList();

        // 构建分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", showList);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", totalPages);
        result.put("hasNext", page < totalPages);
        result.put("hasPrev", page > 1);

        return result;
    }

    @Override
    public Map<String, Object> getShowDetail(Long showId) {
        Show show = showMapper.findById(showId);

        if (show == null) {
            throw new RuntimeException("演出不存在");
        }

        // 构建详情数据（从Controller复制）
        Map<String, Object> result = new HashMap<>();

        // 基本信息
        result.put("id", show.getId());
        result.put("title", show.getTitle());
        result.put("description", show.getDescription());
        result.put("category", show.getCategory());
        result.put("city", show.getCity());
        result.put("venue", show.getVenue());
        result.put("coverImage", show.getCoverImage());
        result.put("startTime", show.getStartTime());
        result.put("endTime", show.getEndTime());
        result.put("minPrice", show.getMinPrice());
        result.put("maxPrice", show.getMaxPrice());

        // 业务状态
        result.put("status", show.getStatus());
        result.put("onSale", show.isOnSale());
        result.put("hasStock", show.hasStock());

        // 场次信息（简化版）
        List<Map<String, Object>> sessions = new ArrayList<>();
        Map<String, Object> session = new HashMap<>();
        session.put("id", 1);
        session.put("time", show.getStartTime());
        session.put("status", show.isOnSale() ? "on_sale" : "not_start");
        sessions.add(session);
        result.put("sessions", sessions);

        // 票档信息（简化版）
        List<Map<String, Object>> ticketTiers = new ArrayList<>();

        Map<String, Object> vipTier = new HashMap<>();
        vipTier.put("id", 1);
        vipTier.put("name", "VIP");
        vipTier.put("price", show.getMaxPrice());
        vipTier.put("stockAvailable", show.hasStock());
        ticketTiers.add(vipTier);

        Map<String, Object> standardTier = new HashMap<>();
        standardTier.put("id", 2);
        standardTier.put("name", "普通票");
        standardTier.put("price", show.getMinPrice());
        standardTier.put("stockAvailable", show.hasStock());
        ticketTiers.add(standardTier);

        result.put("ticketTiers", ticketTiers);

        return result;
    }

    @Override
    @Transactional
    public String seckillTicket(Long userId, Long showId, SeckillRequest request) {
        // 参数校验
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        if (showId == null) {
            throw new RuntimeException("演出ID不能为空");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("购买数量必须大于0");
        }
        if (request.getTicketTierId() == null) {
            throw new RuntimeException("请选择票档");
        }

        // 调用抢票服务
        return seckillService.seckillTicket(userId, showId, request);
    }

    @Override
    public List<String> getAllCities() {
        return showMapper.findAllCities();
    }

    @Override
    public List<String> getAllCategories() {
        return showMapper.findAllCategories();
    }
}
