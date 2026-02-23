package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.User;
import org.apache.ibatis.annotations.*;

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
}
