package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.SeckillRequest;
import com.example.ticketsystem.entity.Order;
import com.example.ticketsystem.entity.TicketTier;
import com.example.ticketsystem.mapper.OrderMapper;
import com.example.ticketsystem.mapper.TicketTierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lark
 * @ date 2025/12/18  20:23
 * @ description 抢票Service
 */
@Service
public class SeckillService {
    @Autowired
    private TicketTierMapper ticketTierMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 简单版抢票核心逻辑
     * @param userId 用户ID
     * @param showId 演出ID
     * @param request 抢票请求
     * @return 订单号
     */
    @Transactional  //事务管理
    //TODO:后续使用Redis优化
    public String seckillTicket(Long userId, Long showId, SeckillRequest request) {
        //检查库存
        Integer currentStock = ticketTierMapper.checkStock(request.getTicketTierId());
        if (currentStock == null) {
            throw new RuntimeException("票档不存在");
        }
        if (currentStock < request.getQuantity()) {
            throw new RuntimeException("库存不足，剩余：" + currentStock);
        }

        //查询票档信息（带版本号）
        TicketTier ticketTier = ticketTierMapper.findById(request.getTicketTierId());

        //扣减库存（乐观锁）
        int rows = ticketTierMapper.reduceStockWithLock(
                request.getTicketTierId(),   //获取票档
                request.getQuantity(),   //获取购买张数
                ticketTier.getVersion()   //获取当前版本号
        );

        //检查是否扣减成功（rows=1表示成功）
        if (rows == 0) {
            throw new RuntimeException("手慢了，票已被抢光");
        }

        //若扣减成功，创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setShowId(showId);
        order.setSessionId(request.getSessionId());
        order.setTicketTierId(request.getTicketTierId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(ticketTier.getPrice());
        order.setTotalAmount(ticketTier.getPrice() * request.getQuantity());
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());

        orderMapper.insert(order);

        return order.getOrderNo();
    }

    //生成订单号
    private String generateOrderNo() {
        return "T" + System.currentTimeMillis() +   //时间戳
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();   //随机数（截取UUID）
    }
}
