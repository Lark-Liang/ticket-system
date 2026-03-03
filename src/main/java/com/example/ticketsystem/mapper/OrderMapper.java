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
            "</script>"
    })
    List<Order> findByUserId(@Param("userId") Long userId,
                             @Param("status") String status);



    //更新订单状态
    @Update("UPDATE `order` SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    // ====== 管理员方法 ======

    /**
     * 管理员条件查询所有订单
     */
    @Select({
            "<script>",
            "SELECT o.* FROM `order` o",
            "LEFT JOIN users u ON o.user_id = u.id",
            "LEFT JOIN show_info s ON o.show_id = s.id",
            "WHERE 1=1",
            "<if test='orderNo != null and orderNo != \"\"'> AND o.order_no = #{orderNo}</if>",
            "<if test='userId != null'> AND o.user_id = #{userId}</if>",
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%')</if>",
            "<if test='showId != null'> AND o.show_id = #{showId}</if>",
            "<if test='showTitle != null and showTitle != \"\"'> AND s.title LIKE CONCAT('%', #{showTitle}, '%')</if>",
            "<if test='status != null and status != \"\"'> AND o.status = #{status}</if>",
            "<if test='startDate != null and startDate != \"\"'> AND DATE(o.created_at) >= #{startDate}</if>",
            "<if test='endDate != null and endDate != \"\"'> AND DATE(o.created_at) <= #{endDate}</if>",
            " ORDER BY o.created_at DESC",
            "</script>"
    })
    List<Order> adminFindByConditions(@Param("orderNo") String orderNo,
                                      @Param("userId") Long userId,
                                      @Param("username") String username,
                                      @Param("showId") Long showId,
                                      @Param("showTitle") String showTitle,
                                      @Param("status") String status,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate);

    /**
     * 查询订单详情（关联用户、演出、票档信息）
     */
    @Select({
            "SELECT o.*, " +
                    "u.username, u.phone as user_phone, u.email as user_email, " +
                    "s.title as show_title, s.cover_image as show_cover, s.venue as show_venue, s.city as show_city, s.start_time as show_start_time, s.end_time as show_end_time, " +
                    "ss.session_time as session_time, " +
                    "t.name as ticket_tier_name, " +
                    "a.name as receiver_name, a.phone as receiver_phone, a.address as receiver_address " +
                    "FROM `order` o " +
                    "LEFT JOIN users u ON o.user_id = u.id " +
                    "LEFT JOIN show_info s ON o.show_id = s.id " +
                    "LEFT JOIN show_session ss ON o.session_id = ss.id " +
                    "LEFT JOIN ticket_tier t ON o.ticket_tier_id = t.id " +
                    "LEFT JOIN address a ON o.address_id = a.id " +
                    "WHERE o.id = #{id}"
    })
    @Results(id = "orderDetailResult", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "username", property = "username"),
            @Result(column = "user_phone", property = "userPhone"),
            @Result(column = "user_email", property = "userEmail"),
            @Result(column = "show_id", property = "showId"),
            @Result(column = "show_title", property = "showTitle"),
            @Result(column = "show_cover", property = "showCover"),
            @Result(column = "show_venue", property = "showVenue"),
            @Result(column = "show_city", property = "showCity"),
            @Result(column = "show_start_time", property = "showStartTime"),
            @Result(column = "show_end_time", property = "showEndTime"),
            @Result(column = "session_id", property = "sessionId"),
            
            @Result(column = "ticket_tier_id", property = "ticketTierId"),
            @Result(column = "ticket_tier_name", property = "ticketTierName"),
            @Result(column = "quantity", property = "quantity"),
            @Result(column = "unit_price", property = "unitPrice"),
            @Result(column = "total_amount", property = "totalAmount"),
            @Result(column = "status", property = "status"),
            @Result(column = "address_id", property = "addressId"),
            @Result(column = "receiver_name", property = "receiverName"),
            @Result(column = "receiver_phone", property = "receiverPhone"),
            @Result(column = "receiver_address", property = "receiverAddress"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    Order findOrderDetailById(@Param("id") Long id);
}
