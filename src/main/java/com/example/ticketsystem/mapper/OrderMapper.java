package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.Order;
import org.apache.ibatis.annotations.*;

/**
 * @author Lark
 * @ date 2025/12/18  20:17
 * @ description 订单Mapper
 */
@Mapper
public interface OrderMapper {
    // 插入订单
    @Insert("INSERT INTO `order` (order_no, user_id, show_id, session_id, " +
            "ticket_tier_id, quantity, unit_price, total_amount, status) " +
            "VALUES (#{orderNo}, #{userId}, #{showId}, #{sessionId}, " +
            "#{ticketTierId}, #{quantity}, #{unitPrice}, #{totalAmount}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    // 根据订单号查询
    @Select("SELECT * FROM `order` WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);
}
