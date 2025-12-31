package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.entity.Order;
import com.example.ticketsystem.mapper.OrderMapper;
import com.example.ticketsystem.mapper.ShowMapper;
import com.example.ticketsystem.mapper.TicketTierMapper;
import com.example.ticketsystem.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    private OrderMapper orderMapper;
    @Autowired
    private ShowMapper showMapper;
    @Autowired
    private TicketTierMapper ticketTierMapper;
    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 分页查询订单列表
     * GET /orders/list
     * Query Parameters:
     * - status: 订单状态(可选，pending/paid/cancelled)
     * - page: 页码(可选，默认1)
     * - size: 每页数量(可选，默认10)
     */
    @GetMapping("/list")
    public ApiResponse<?> getOrderList(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = tokenUtil.extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        //计算分页
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;
        int offset = (page - 1) * size;

        //查询订单
        List<Order> orders = orderMapper.findByUserId(userId, status, offset, size);
        int total = orderMapper.countByUserId(userId, status);

        //构建响应
        Map<String, Object> result = new HashMap<>();
        result.put("list", orders);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        result.put("hasNext", page < Math.ceil((double) total / size));
        result.put("hasPrev", page > 1);

        return ApiResponse.success(result);
    }

    /**
     * 获取订单详情
     * GET /orders/{id}
     * Path Parameters:
     *  - id: 订单ID(必填)
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getOrderDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long userId = tokenUtil.extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        //查询订单
        Order order = orderMapper.findById(id);
        if (order == null) {
            return ApiResponse.error(404, "订单不存在");
        }

        //检查权限（只能查看自己的订单）
        if (!order.getUserId().equals(userId)) {
            return ApiResponse.error(403, "无权查看此订单");
        }

        // TODO: 这里后续可以关联查询更多信息，比如演出标题、票档名称、收货地址等

        return ApiResponse.success(order);
    }

    /**
     * 取消订单
     * POST /orders/{id}/cancel
     * Path Parameters:
     *  - id: 订单ID(必填)
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse<?> cancelOrder(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long userId = tokenUtil.extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        //查询订单
        Order order = orderMapper.findById(id);
        if (order == null) {
            return ApiResponse.error(404, "订单不存在");
        }

        //检查权限
        if (!order.getUserId().equals(userId)) {
            return ApiResponse.error(403, "无权操作此订单");
        }

        //检查状态（只能取消待支付订单）
        if (!"pending".equals(order.getStatus())) {
            return ApiResponse.error(400, "只能取消待支付订单，当前状态: " + order.getStatus());
        }

        //更新订单状态为cancelled
        int updateRows = orderMapper.updateStatus(id, "cancelled");
        if (updateRows == 0) {
            return ApiResponse.error(500, "更新订单状态失败");
        }

        //恢复库存
        // TODO: 这里需要调用TicketTierMapper恢复库存

        return ApiResponse.success("取消成功");
    }
}
