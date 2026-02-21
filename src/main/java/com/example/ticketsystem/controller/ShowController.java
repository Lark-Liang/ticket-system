package com.example.ticketsystem.controller;

import com.example.ticketsystem.annotation.PassToken;
import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.SeckillRequest;
import com.example.ticketsystem.service.ShowService;
import com.example.ticketsystem.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private ShowService showService;

    /**
     * 首页演出列表
     */
    @GetMapping("/home")
    public ApiResponse<Object> getHomeShows(
            @RequestParam(defaultValue = "北京") String city,
            @RequestParam(defaultValue = "10") int limit) {

        List<Map<String, Object>> result = showService.getHomeShows(city, limit);
        return ApiResponse.success(result);
    }

    /**
     * 搜索演出（游客也可以访问）
     */
    @GetMapping("/search")
    @PassToken
    public ApiResponse<Object> searchShows(@RequestParam String keyword) {
        try {
            List<Map<String, Object>> result = showService.searchShows(keyword);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 条件查询演出（分页）（游客也可以访问）
     */
    @GetMapping("/list")
    @PassToken
    public ApiResponse<Object> listShows(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> result = showService.listShows(city, category, page, size);
        return ApiResponse.success(result);
    }

    /**
     * 辅助接口：获取所有有演出的城市列表（游客也可以访问）
     */
    @GetMapping("/cities")
    @PassToken
    public ApiResponse<Object> getAllCities() {
        List<String> cities = showService.getAllCities();
        return ApiResponse.success(cities);
    }

    /**
     * 辅助接口：获取所有演出分类列表（游客也可以访问）
     */
    @GetMapping("/categories")
    @PassToken
    public ApiResponse<Object> getAllCategories() {
        List<String> categories = showService.getAllCategories();
        return ApiResponse.success(categories);
    }

    /**
     * 获取演出详情（游客也可以访问）
     */
    @GetMapping("/{id}")
    @PassToken
    public ApiResponse<?> getShowDetail(@PathVariable Long id) {
        try {
            Map<String, Object> result = showService.getShowDetail(id);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }

    /**抢票接口
     */
    @PostMapping("/{showId}/tickets/seckill")
    public ApiResponse<?> seckillTicket(
            HttpServletRequest request,
            @PathVariable Long showId,
            @RequestBody SeckillRequest seckillRequest){   //从请求体获取JSON参数

        Long userId = (Long) request.getAttribute("userId");

        try {
            String orderNo = showService.seckillTicket(userId, showId, seckillRequest);

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
