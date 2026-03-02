package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.OrderDTO;
import com.example.ticketsystem.dto.OrderDetailDTO;
import com.example.ticketsystem.entity.Order;
import com.example.ticketsystem.mapper.OrderMapper;
import com.example.ticketsystem.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/2/13  20:42
 * @ description 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ListResponseDTO<OrderDTO> getUserOrderList(Long userId, String status, int page, int size) {
        // 参数校验
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        // 计算分页
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;

        // 使用 PageHelper 开始分页
        PageHelper.startPage(page, size);

        // 查询订单
        List<Order> orders = orderMapper.findByUserId(userId, status);

        // 使用 PageInfo 获取分页信息
        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderDTO::fromOrder)
                .toList();

        return ListResponseDTO.of(
                orderDTOs,
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
    }

    @Override
    public OrderDetailDTO getOrderDetail(Long userId, Long orderId) {
        if (userId == null || orderId == null) {
            throw new RuntimeException("参数不能为空");
        }

        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此订单");
        }
        return OrderDetailDTO.fromOrder(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        if (userId == null || orderId == null) {
            throw new RuntimeException("参数不能为空");
        }

        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("只能取消待支付订单，当前状态: " + order.getStatus());
        }

        int updateRows = orderMapper.updateStatus(orderId, "cancelled");
        if (updateRows == 0) {
            throw new RuntimeException("更新订单状态失败");
        }

        // TODO: 恢复库存逻辑
    }
}
