package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminOrderDetailDTO;
import com.example.ticketsystem.dto.admin.AdminOrderListDTO;
import com.example.ticketsystem.dto.admin.AdminOrderQueryDTO;
import com.example.ticketsystem.dto.admin.AdminOrderStatusUpdateDTO;
import com.example.ticketsystem.entity.Order;
import com.example.ticketsystem.mapper.OrderMapper;
import com.example.ticketsystem.service.AdminOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lark
 * @ date 2026/3/2  22:29
 * @ description 管理员订单管理服务接口
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService{
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ListResponseDTO<AdminOrderListDTO> getOrderList(AdminOrderQueryDTO queryDTO) {
        // 参数处理
        if (queryDTO.getPage() < 1) queryDTO.setPage(1);
        if (queryDTO.getSize() < 1 || queryDTO.getSize() > 100) queryDTO.setSize(10);

        // 使用 PageHelper 开始分页
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getSize());

        // 执行查询
        List<Order> orders = orderMapper.adminFindByConditions(
                queryDTO.getOrderNo(),
                queryDTO.getUserId(),
                queryDTO.getUsername(),
                queryDTO.getShowId(),
                queryDTO.getShowTitle(),
                queryDTO.getStatus(),
                queryDTO.getStartDate(),
                queryDTO.getEndDate()
        );

        // 获取分页信息
        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        // 转换为 DTO
        List<AdminOrderListDTO> orderList = orders.stream()
                .map(this::convertToAdminOrderListDTO)
                .collect(Collectors.toList());

        return ListResponseDTO.of(
                orderList,
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
    }

    @Override
    public AdminOrderDetailDTO getOrderDetail(Long orderId) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }

        // 使用关联查询获取完整订单信息
        Order order = orderMapper.findOrderDetailById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        return convertToAdminOrderDetailDTO(order);
    }

    @Override
    @Transactional
    public AdminOrderDetailDTO updateOrderStatus(Long orderId, AdminOrderStatusUpdateDTO updateDTO) {
        if (orderId == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        if (updateDTO.getStatus() == null || updateDTO.getStatus().trim().isEmpty()) {
            throw new RuntimeException("订单状态不能为空");
        }

        // 验证状态值
        String newStatus = updateDTO.getStatus();
        if (!isValidStatus(newStatus)) {
            throw new RuntimeException("无效的订单状态，只能是：pending, paid, cancelled");
        }

        // 查询订单是否存在
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查状态转换是否允许
        String currentStatus = order.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException(String.format("不允许从 %s 状态转换为 %s 状态", currentStatus, newStatus));
        }

        // 更新状态
        int rows = orderMapper.updateStatus(orderId, newStatus);
        if (rows == 0) {
            throw new RuntimeException("更新订单状态失败");
        }

        // 返回更新后的订单详情
        return getOrderDetail(orderId);
    }

    // ====== 私有辅助方法 ======

    private AdminOrderListDTO convertToAdminOrderListDTO(Order order) {
        AdminOrderListDTO dto = new AdminOrderListDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setUserId(order.getUserId());
        dto.setUsername(order.getUsername());  // 来自关联查询
        dto.setShowId(order.getShowId());
        dto.setShowTitle(order.getShowTitle());  // 来自关联查询
        dto.setShowCover(order.getShowCover());  // 来自关联查询
        dto.setSessionTime(order.getSessionTime());
        dto.setTicketTierName(order.getTicketTierName());  // 来自关联查询
        dto.setQuantity(order.getQuantity());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    private AdminOrderDetailDTO convertToAdminOrderDetailDTO(Order order) {
        AdminOrderDetailDTO dto = new AdminOrderDetailDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());

        // 用户信息
        dto.setUserId(order.getUserId());
        dto.setUsername(order.getUsername());
        dto.setUserPhone(order.getUserPhone());
        dto.setUserEmail(order.getUserEmail());

        // 演出信息
        dto.setShowId(order.getShowId());
        dto.setShowTitle(order.getShowTitle());
        dto.setShowCover(order.getShowCover());
        dto.setShowVenue(order.getShowVenue());
        dto.setShowCity(order.getShowCity());
        dto.setShowStartTime(order.getShowStartTime());
        dto.setShowEndTime(order.getShowEndTime());

        // 场次信息
        dto.setSessionId(order.getSessionId());
        dto.setSessionTime(order.getSessionTime());

        // 票档信息
        dto.setTicketTierId(order.getTicketTierId());
        dto.setTicketTierName(order.getTicketTierName());
        dto.setUnitPrice(order.getUnitPrice());

        // 订单信息
        dto.setQuantity(order.getQuantity());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        // 地址信息
        dto.setAddressId(order.getAddressId());
        dto.setReceiverName(order.getReceiverName());
        dto.setReceiverPhone(order.getReceiverPhone());
        dto.setReceiverAddress(order.getReceiverAddress());

        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }

    private boolean isValidStatus(String status) {
        return "pending".equals(status) || "paid".equals(status) || "cancelled".equals(status);
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals(newStatus)) {
            return true;  // 相同状态允许
        }

        switch (currentStatus) {
            case "pending":
                // 待支付可以改为已支付或已取消
                return "paid".equals(newStatus) || "cancelled".equals(newStatus);
            case "paid":
                // 已支付只能改为已取消（退款场景），通常不允许
                return "cancelled".equals(newStatus);
            case "cancelled":
                // 已取消不能改为其他状态
                return false;
            default:
                return false;
        }
    }
}
