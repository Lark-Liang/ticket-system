package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2025/12/12  16:37
 * @ description
 */
@Mapper
public interface AddressMapper {

    //根据用户ID查询地址列表
    @Select("SELECT * FROM address WHERE user_id = #{userId} ORDER BY is_default DESC, id DESC")
    List<Address> findByUserId(Long userId);

    //根据ID查询地址
    @Select("SELECT * FROM address WHERE id = #{id}")
    Address findById(Long id);

    //插入新地址
    @Insert("INSERT INTO address (user_id, name, phone, address, is_default) " +
            "VALUES (#{userId}, #{name}, #{phone}, #{address}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address address);

    //更新地址
    @Update("UPDATE address SET name=#{name}, phone=#{phone}, address=#{address}, " +
            "is_default=#{isDefault}, updated_at=NOW() WHERE id=#{id}")
    int update(Address address);

    //删除地址
    @Delete("DELETE FROM address WHERE id = #{id}")
    int delete(Long id);

    //取消用户的所有默认地址
    @Update("UPDATE address SET is_default = 0 WHERE user_id = #{userId}")
    int cancelAllDefault(Long userId);
}
