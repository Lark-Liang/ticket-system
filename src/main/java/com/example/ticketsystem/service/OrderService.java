package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.OrderDTO;
import com.example.ticketsystem.dto.OrderDetailDTO;

/**
 * @author Lark
 * @ date 2026/2/13  20:41
 * @ description 订单服务接口
 */
public interface OrderService {
    //分页查询用户订单
    ListResponseDTO<OrderDTO> getUserOrderList(Long userId, String status, int page, int size);

    //获取订单详情
    OrderDetailDTO getOrderDetail(Long userId, Long orderId);

    //取消订单
    void cancelOrder(Long userId, Long orderId);
}
