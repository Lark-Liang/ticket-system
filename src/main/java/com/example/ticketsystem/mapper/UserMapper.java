package com.example.ticketsystem.mapper;

import com.example.ticketsystem.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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
    @Insert("INSERT INTO users (username, password, email, nickname, phone, role, status) " +
            "VALUES (#{username}, #{password}, #{email}, #{nickname}, #{phone}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
}
