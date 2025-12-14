package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.entity.Show;
import com.example.ticketsystem.mapper.ShowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/12  20:14
 * @ description
 */
@RestController
@RequestMapping("/shows")
public class ShowController {
    @Autowired
    private ShowMapper showMapper;

    /**
     * 1. 首页演出列表
     * GET /shows/home?city=北京
     */
    @GetMapping("/home")
    public ApiResponse<Object> getHomeShows(
            @RequestParam(defaultValue = "北京") String city,
            @RequestParam(defaultValue = "10") int limit) {

        List<Show> shows = showMapper.findHomeShows(city, limit);

        // 转换响应格式（隐藏敏感信息，添加业务字段）
        List<Map<String, Object>> result = shows.stream().map(show -> {
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
            map.put("hasStock", show.hasStock());  // 用户端只关心是否有库存
            map.put("onSale", show.isOnSale());    // 是否已开票
            return map;
        }).toList();

        return ApiResponse.success(result);
    }

    /**
     * 2. 搜索演出
     * GET /shows/search?keyword=周杰伦
     */
    @GetMapping("/search")
    public ApiResponse<Object> searchShows(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ApiResponse.error(400, "搜索关键词不能为空");
        }

        List<Show> shows = showMapper.searchByKeyword(keyword.trim());

        // 简化响应（和首页列表类似）
        List<Map<String, Object>> result = shows.stream().map(show -> {
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

        return ApiResponse.success(result);
    }

    /**
     * 3. 条件查询演出（分页）
     * GET /shows/list?city=北京&category=concert&page=1&size=10
     */
    @GetMapping("/list")
    public ApiResponse<Object> listShows(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 参数验证
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;

        int offset = (page - 1) * size;

        // 查询数据
        List<Show> shows = showMapper.findByConditions(city, category, offset, size);
        int total = showMapper.countByConditions(city, category);
        int totalPages = (int) Math.ceil((double) total / size);

        // 构建响应
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

        Map<String, Object> result = new HashMap<>();
        result.put("list", showList);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", totalPages);
        result.put("hasNext", page < totalPages);
        result.put("hasPrev", page > 1);

        return ApiResponse.success(result);
    }

    /**
     * 辅助接口：获取所有城市（用于前端筛选器）
     */
    @GetMapping("/cities")
    public ApiResponse<Object> getAllCities() {
        List<String> cities = showMapper.findAllCities();
        return ApiResponse.success(cities);
    }

    /**
     * 辅助接口：获取所有分类（用于前端筛选器）
     */
    @GetMapping("/categories")
    public ApiResponse<Object> getAllCategories() {
        List<String> categories = showMapper.findAllCategories();
        return ApiResponse.success(categories);
    }
}
