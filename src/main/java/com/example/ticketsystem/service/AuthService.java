package com.example.ticketsystem.service;

import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lark
 * @ date 2025/12/11  20:53
 * @ description：认证模块，负责处理用户登录验证等
 */

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        //根据用户名查询当前用户是否存在
        User user = userMapper.findByUsername(username);
        //简单密码验证（这里先用临时设定的简单明文密码，实际应该用BCrypt加密？）
        if (user != null && "123456".equals(password)) {
            return user;
        }

        return null;
    }
}
