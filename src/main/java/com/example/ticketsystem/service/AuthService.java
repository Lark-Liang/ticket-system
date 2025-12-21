package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.AuthRequest;
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

    /**
     * 登录/注册二合一
     * @return 用户信息，null表示失败
     */
    public User auth(AuthRequest request) {
        //查找用户是否存在
        User user = userMapper.findByUsername(request.getUsername());
        if (user != null) {
            //用户存在:登录
            return login(user, request.getPassword());
        } else {
            //用户不存在:注册
            return register(request);
        }
    }

    /**
     * 登录验证
     */
    public User login(User user, String password) {
        //简单密码验证
        // TODO：这里先用临时设定的简单明文密码，后续可用BCrypt加密
        if (user != null && "123456".equals(password)) {
            //检查用户状态
            if (user.getStatus() != null && user.getStatus() == 0) {
                throw new RuntimeException("用户已被禁用");
            }
            return user;
        }else {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    /**
     * 注册新用户
     */
    private User register(AuthRequest request) {
        //基本验证
        if (!request.isValidForRegister()) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        //检查用户名是否已存在
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        //创建用户对象
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname() != null ?
                request.getNickname() : request.getUsername());
        user.setPhone(request.getPhone());
        user.setRole("user");   //默认普通用户
        user.setStatus(1);   //默认启用

        //插入数据库
        int result = userMapper.insert(user);
        if (result > 0) {
            return user;
        } else {
            throw new RuntimeException("注册失败，请重试");
        }
    }

    //邮箱登录
    public User loginByEmail(String email, String password) {
        //根据邮箱查询用户
        User user = userMapper.findByEmail(email);

        //验证密码
        if (user != null && "123456".equals(password)) {
            return user;
        }
        return null;
    }
}
