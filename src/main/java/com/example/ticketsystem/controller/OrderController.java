package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.entity.Order;
import com.example.ticketsystem.service.OrderService;
import com.example.ticketsystem.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Lark
 * @ date 2025/12/19  19:21
 * @ description 订单模块，含分页查询订单列表、获取订单详情、取消订单
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     */
    @GetMapping("/list")
    public ApiResponse<?> getOrderList(
            HttpServletRequest request,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = (Long) request.getAttribute("userId");

        try {
            Map<String, Object> result = orderService.getUserOrderList(userId, status, page, size);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getOrderDetail(
            HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = (Long) request.getAttribute("userId");

        try {
            Order order = orderService.getOrderDetail(userId, id);
            return ApiResponse.success(order);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(403, e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse<?> cancelOrder(
            HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = (Long) request.getAttribute("userId");

        try {
            orderService.cancelOrder(userId, id);
            return ApiResponse.success("取消成功");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
