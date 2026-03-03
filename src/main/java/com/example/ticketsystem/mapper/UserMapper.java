package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Lark
 * @ date 2025/12/11  20:52
 * @ description：用户数据访问层接口，负责用户信息的数据库查询操作
 */
@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    //根据ID查询用户
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
    //根据邮箱查询用户
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    //插入用户
    @Insert("INSERT INTO users (username, password, email, nickname,avatar, bio, gender, birthday, background_image, phone, role, status) " +
            "VALUES (#{username}, #{password}, #{email}, #{nickname},#{avatar}, #{bio}, #{gender}, #{birthday}, #{backgroundImage}, #{phone}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    // 更新用户信息
    @Update("<script>" +
            "UPDATE users SET updated_at=NOW()" +
            "<if test='nickname != null'> , nickname=#{nickname}</if>" +
            "<if test='avatar != null'> , avatar=#{avatar}</if>" +
            "<if test='bio != null'> , bio=#{bio}</if>" +
            "<if test='gender != null'> , gender=#{gender}</if>" +
            "<if test='birthday != null'> , birthday=#{birthday}</if>" +
            "<if test='backgroundImage != null'> , background_image=#{backgroundImage}</if>" +
            "<if test='phone != null'> , phone=#{phone}</if>" +
            "<if test='email != null'> , email=#{email}</if>" +
            " WHERE id = #{id}" +
            "</script>")
    int update(User user);

    // ====== 管理员查询方法 ======

    /**
     * 条件查询用户列表（分页由PageHelper处理）
     */
    @Select({
            "<script>",
            "SELECT * FROM users WHERE 1=1",
            "<if test='username != null and username != \"\"'> AND username LIKE CONCAT('%', #{username}, '%')</if>",
            "<if test='phone != null and phone != \"\"'> AND phone LIKE CONCAT('%', #{phone}, '%')</if>",
            "<if test='email != null and email != \"\"'> AND email LIKE CONCAT('%', #{email}, '%')</if>",
            "<if test='status != null'> AND status = #{status}</if>",
            "<if test='role != null and role != \"\"'> AND role = #{role}</if>",
            "<if test='startDate != null and startDate != \"\"'> AND DATE(created_at) >= #{startDate}</if>",
            "<if test='endDate != null and endDate != \"\"'> AND DATE(created_at) <= #{endDate}</if>",
            " ORDER BY created_at DESC",
            "</script>"
    })
    List<User> findByConditions(@Param("username") String username,
                                @Param("phone") String phone,
                                @Param("email") String email,
                                @Param("status") Integer status,
                                @Param("role") String role,
                                @Param("startDate") String startDate,
                                @Param("endDate") String endDate);

    /**
     * 更新用户状态（启用/禁用）
     */
    @Update("UPDATE users SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
