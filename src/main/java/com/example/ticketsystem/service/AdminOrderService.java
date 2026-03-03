package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminOrderDetailDTO;
import com.example.ticketsystem.dto.admin.AdminOrderListDTO;
import com.example.ticketsystem.dto.admin.AdminOrderQueryDTO;
import com.example.ticketsystem.dto.admin.AdminOrderStatusUpdateDTO;

/**
 * @author Lark
 * @ date 2026/3/2  22:27
 * @ description 管理员订单管理服务接口
 */
public interface AdminOrderService {
    //分页查询订单列表
    ListResponseDTO<AdminOrderListDTO> getOrderList(AdminOrderQueryDTO queryDTO);

    //查询订单详情
    AdminOrderDetailDTO getOrderDetail(Long orderId);

    //修改订单状态
    AdminOrderDetailDTO updateOrderStatus(Long orderId, AdminOrderStatusUpdateDTO updateDTO);
}
