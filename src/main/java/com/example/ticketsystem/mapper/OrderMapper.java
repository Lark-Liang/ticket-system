package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.Order;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @author Lark
 * @ date 2025/12/18  20:17
 * @ description 订单Mapper
 */
@Mapper
public interface OrderMapper {
    //插入订单
    @Insert("INSERT INTO `order` (order_no, user_id, show_id, session_id, " +
            "ticket_tier_id, quantity, unit_price, total_amount, status) " +
            "VALUES (#{orderNo}, #{userId}, #{showId}, #{sessionId}, " +
            "#{ticketTierId}, #{quantity}, #{unitPrice}, #{totalAmount}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Select("SELECT * FROM `order` WHERE id = #{id}")
    Order findById(Long id);

    //根据订单号查询
    @Select("SELECT * FROM `order` WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);

    //分页查询用户订单
    @Select({
            "<script>",
            "SELECT * FROM `order` WHERE user_id = #{userId}",
            "<if test='status != null'> AND status = #{status} </if>",
            " ORDER BY created_at DESC",
            " LIMIT #{offset}, #{size}",
            "</script>"
    })
    List<Order> findByUserId(@Param("userId") Long userId,
                             @Param("status") String status,
                             @Param("offset") int offset,
                             @Param("size") int size);

    //统计用户订单数量
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM `order` WHERE user_id = #{userId}",
            "<if test='status != null'> AND status = #{status} </if>",
            "</script>"
    })
    int countByUserId(@Param("userId") Long userId,
                      @Param("status") String status);

    //更新订单状态
    @Update("UPDATE `order` SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

}
