package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.TicketTier;
import org.apache.ibatis.annotations.*;

/**
 * @author Lark
 * @ date 2025/12/18  20:11
 * @ description 票档Mapper
 */
@Mapper
public interface TicketTierMapper {
    // 根据ID查询票档（带乐观锁版本号）
    @Select("SELECT * FROM TICKET_TIER WHERE id = #{id}")
    TicketTier findById(Long id);

    // 扣减库存（乐观锁实现）
    @Update("UPDATE TICKET_TIER SET " +
            "available_stock = available_stock - #{quantity}, " +
            "version = version + 1, " +
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{id} " +
            "AND available_stock >= #{quantity} " +
            "AND version = #{version}")
    int reduceStockWithLock(@Param("id") Long id,
                            @Param("quantity") Integer quantity,
                            @Param("version") Integer version);

    // 检查库存是否充足
    @Select("SELECT available_stock FROM ticket_tier WHERE id = #{id}")
    Integer checkStock(Long id);
}
