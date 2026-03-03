package com.example.ticketsystem.controller.admin;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminOrderDetailDTO;
import com.example.ticketsystem.dto.admin.AdminOrderListDTO;
import com.example.ticketsystem.dto.admin.AdminOrderQueryDTO;
import com.example.ticketsystem.dto.admin.AdminOrderStatusUpdateDTO;
import com.example.ticketsystem.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/3/2  22:33
 * @ description 管理员订单管理控制器
 */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private AdminOrderService adminOrderService;

    /**
     * 分页查询订单列表
     */
    @GetMapping("/list")
    public ApiResponse<?> getOrderList(AdminOrderQueryDTO queryDTO) {
        try {
            ListResponseDTO<AdminOrderListDTO> result = adminOrderService.getOrderList(queryDTO);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getOrderDetail(@PathVariable Long id) {
        try {
            AdminOrderDetailDTO orderDetail = adminOrderService.getOrderDetail(id);
            return ApiResponse.success(orderDetail);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 修改订单状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody AdminOrderStatusUpdateDTO updateDTO) {
        try {
            AdminOrderDetailDTO orderDetail = adminOrderService.updateOrderStatus(id, updateDTO);
            return ApiResponse.success("订单状态更新成功", orderDetail);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
