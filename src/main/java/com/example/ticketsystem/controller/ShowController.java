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
 * @ description: 演出模块，含首页演出列表接口、搜索演出接口、条件查询演出接口、获取演出详情接口、抢票接口及提供给前端的辅助接口
 */
@RestController
@RequestMapping("/shows")
public class ShowController {
    @Autowired
    private ShowMapper showMapper;

    /**
     * 首页演出列表
     * GET /shows/home
     * Query Parameters:
     *  - city:城市(可选，默认“北京”)
     *  - limit:返回数量(可选，默认10)
     */
    @GetMapping("/home")
    public ApiResponse<Object> getHomeShows(
            @RequestParam(defaultValue = "北京") String city,
            @RequestParam(defaultValue = "10") int limit) {

        List<Show> shows = showMapper.findHomeShows(city, limit);

        //转换响应格式（隐藏敏感信息，添加业务字段）
        List<Map<String, Object>> result = shows.stream().map(show -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", show.getId());   //演出ID
            map.put("title", show.getTitle());   //演出标题
            map.put("category", show.getCategory());   //演出分类(concert,drama...)
            map.put("city", show.getCity());   //所在城市
            map.put("coverImage", show.getCoverImage());   //封面图
            map.put("venue", show.getVenue());   //场馆
            map.put("startTime", show.getStartTime());   //开始时间
            map.put("endTime", show.getEndTime());   //结束时间
            map.put("minPrice", show.getMinPrice());   //最低价
            map.put("maxPrice", show.getMaxPrice());   //最高价
            map.put("hasStock", show.hasStock());   //是否有库存
            map.put("onSale", show.isOnSale());   //是否已开票
            return map;
        }).toList();

        return ApiResponse.success(result);
    }

    /**
     * 搜索演出
     * GET /shows/search
     * Query Parameters:
     *  - keyword:搜索关键词(必填)
     */
    @GetMapping("/search")
    public ApiResponse<Object> searchShows(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ApiResponse.error(400, "搜索关键词不能为空");
        }

        List<Show> shows = showMapper.searchByKeyword(keyword.trim());

        //响应（和首页列表类似）
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
     * 条件查询演出（分页）
     * GET /shows/list
     * Query Parameters:
     *  - city:城市(可选)
     *  - category:分类(可选，如"concert","drama")
     *  - page:页码(可选，默认1)
     *  - size:每页条数(可选，默认0)
     */
    @GetMapping("/list")
    public ApiResponse<Object> listShows(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        //参数验证，防止无意义或过大过小页码导致问题
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;

        int offset = (page - 1) * size;

        //查询数据
        List<Show> shows = showMapper.findByConditions(city, category, offset, size);
        int total = showMapper.countByConditions(city, category);
        int totalPages = (int) Math.ceil((double) total / size);

        //响应
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
        result.put("hasNext", page < totalPages);   //可以下一页
        result.put("hasPrev", page > 1);   //可以上一页

        return ApiResponse.success(result);
    }

    /**
     * 辅助接口：获取所有有演出的城市列表
     * GET /shows/cities
     */
    @GetMapping("/cities")
    public ApiResponse<Object> getAllCities() {
        List<String> cities = showMapper.findAllCities();
        return ApiResponse.success(cities);
    }

    /**
     * 辅助接口：获取所有演出分类列表
     * GET /shows/categories
     */
    @GetMapping("/categories")
    public ApiResponse<Object> getAllCategories() {
        List<String> categories = showMapper.findAllCategories();
        return ApiResponse.success(categories);
    }
}
