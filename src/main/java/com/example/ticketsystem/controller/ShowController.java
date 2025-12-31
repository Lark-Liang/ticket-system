package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.SeckillRequest;
import com.example.ticketsystem.entity.Show;
import com.example.ticketsystem.mapper.ShowMapper;
import com.example.ticketsystem.service.SeckillService;
import com.example.ticketsystem.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/12  20:14
 * @ description 演出模块，含首页演出列表接口、搜索演出接口、条件查询演出接口、获取演出详情接口、抢票接口及提供给前端的辅助接口
 */
@RestController
@RequestMapping("/shows")
public class ShowController {
    @Autowired
    private ShowMapper showMapper;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private TokenUtil tokenUtil;

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

        //构建响应数据
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

        //构建响应数据
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

        //构建响应数据
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

    /**
     * 获取演出详情
     * GET /shows/{id}
     * Path Parameters:
     *  - id:演出ID(必填)
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getShowDetail(@PathVariable Long id) {
        //查询演出基本信息
        Show show = showMapper.findById(id);

        if (show == null) {
            return ApiResponse.error(404, "演出不存在");
        }

        //响应数据
        Map<String, Object> result = new HashMap<>();
        //基本信息
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
        //业务状态
        result.put("status", show.getStatus());
        result.put("onSale", show.isOnSale());
        result.put("hasStock", show.hasStock());
        //场次信息
        // TODO：此处为简化版，实际可能有多个场次
        List<Map<String, Object>> sessions = new ArrayList<>();
        Map<String, Object> session = new HashMap<>();
        session.put("id", 1);
        session.put("time", show.getStartTime());
        session.put("status", show.isOnSale() ? "on_sale" : "not_start");
        sessions.add(session);
        result.put("sessions", sessions);
        //票档信息
        // TODO：此处为简化版，实际可能有多个票档）
        List<Map<String, Object>> ticketTiers = new ArrayList<>();
        //VIP票档
        Map<String, Object> vipTier = new HashMap<>();
        vipTier.put("id", 1);
        vipTier.put("name", "VIP");
        vipTier.put("price", show.getMaxPrice());
        vipTier.put("stockAvailable", show.hasStock());
        ticketTiers.add(vipTier);
        //普通票档
        Map<String, Object> standardTier = new HashMap<>();
        standardTier.put("id", 2);
        standardTier.put("name", "普通票");
        standardTier.put("price", show.getMinPrice());
        standardTier.put("stockAvailable", show.hasStock());
        ticketTiers.add(standardTier);

        result.put("ticketTiers", ticketTiers);

        //返回
        return ApiResponse.success(result);
    }

    /**抢票接口
     * POST /shows/{showId}/tickets/seckill
     * Headers: Authorization: Bearer {token}
     * Path Parameters:
     *  - showId: 演出ID(必填)
     * Body Parameters:
     *  - sessionId: 场次ID(必填)
     *  - ticketTierId: 票档ID(必填)
     *  - quantity: 购买数量(必填，必须大于0)
     */
    @PostMapping("/{showId}/tickets/seckill")
    public ApiResponse<?> seckillTicket(
            @PathVariable Long showId,   //从URL路径获取演出ID
            @RequestHeader("Authorization") String authHeader,   //从请求头获取token
            @RequestBody SeckillRequest request) {   //从请求体获取JSON参数

        try {
            //解析token
            Long userId = tokenUtil.extractUserIdFromToken(authHeader);
            if (userId == null) {
                return ApiResponse.error(401, "未授权");
            }

            //参数验证
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                return ApiResponse.error(400, "购买数量必须大于0");
            }
            if (request.getTicketTierId() == null) {
                return ApiResponse.error(400, "请选择票档");
            }

            //执行抢票逻辑
            String orderNo = seckillService.seckillTicket(userId, showId, request);

            //返回成功响应
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderNo);
            return ApiResponse.success("抢票成功", data);

        } catch (RuntimeException e) {
            //捕获业务异常
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            //其他异常
            e.printStackTrace();
            return ApiResponse.error(500, "系统繁忙，请稍后重试");
        }
    }
}
