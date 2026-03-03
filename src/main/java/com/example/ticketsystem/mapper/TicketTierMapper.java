package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.TicketTier;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2025/12/18  20:11
 * @ description 票档Mapper
 */
@Mapper
public interface TicketTierMapper {
    //根据ID查询票档（带乐观锁版本号）
    @Select("SELECT * FROM ticket_tier WHERE id = #{id}")
    TicketTier findById(Long id);

    //扣减库存（乐观锁）
    @Update("UPDATE TICKET_TIER SET " +
            "available_stock = available_stock - #{quantity}, " +
            "version = version + 1, " +
            "updated_at = NOW() " +
            "WHERE id = #{id} " +
            "AND available_stock >= #{quantity} " +
            "AND version = #{version}")
    int reduceStockWithLock(@Param("id") Long id,
                            @Param("quantity") Integer quantity,
                            @Param("version") Integer version);

    //检查库存是否充足
    @Select("SELECT available_stock FROM ticket_tier WHERE id = #{id}")
    Integer checkStock(Long id);

    // ====== 管理员方法 ======

    /**
     * 根据演出ID查询所有票档
     */
    @Select("SELECT * FROM ticket_tier WHERE show_id = #{showId} ORDER BY price DESC")
    List<TicketTier> findByShowId(@Param("showId") Long showId);

    /**
     * 批量插入票档
     */
    @Insert({
            "<script>",
            "INSERT INTO ticket_tier (show_id, name, description, price, total_stock, available_stock, status) VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.showId}, #{item.name}, #{item.description}, #{item.price}, #{item.totalStock}, #{item.availableStock}, #{item.status})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int batchInsert(@Param("list") List<TicketTier> ticketTiers);

    /**
     * 更新票档
     */
    @Update("<script>" +
            "UPDATE ticket_tier SET updated_at = NOW()" +
            "<if test='name != null'> , name = #{name}</if>" +
            "<if test='description != null'> , description = #{description}</if>" +
            "<if test='price != null'> , price = #{price}</if>" +
            "<if test='totalStock != null'> , total_stock = #{totalStock}</if>" +
            "<if test='availableStock != null'> , available_stock = #{availableStock}</if>" +
            "<if test='status != null'> , status = #{status}</if>" +
            " WHERE id = #{id}" +
            "</script>")
    int update(TicketTier ticketTier);

    /**
     * 删除票档（物理删除）
     */
    @Delete("DELETE FROM ticket_tier WHERE id = #{id}")
    int delete(@Param("id") Long id);

    /**
     * 根据演出ID删除所有票档
     */
    @Delete("DELETE FROM ticket_tier WHERE show_id = #{showId}")
    int deleteByShowId(@Param("showId") Long showId);
}
