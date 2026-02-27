package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.AuthRequest;
import com.example.ticketsystem.dto.TokenResponse;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.UserMapper;
import com.example.ticketsystem.service.AuthService;
import com.example.ticketsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lark
 * @ date 2026/2/14  21:04
 * @ description 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public TokenResponse auth(AuthRequest request) {
        //查找用户是否存在
        User user = userMapper.findByUsername(request.getUsername());
        boolean isNewUser = false;
        if (user != null) {
            //用户存在:登录
            user = login(user, request.getPassword());
        } else {
            //用户不存在:注册
            user = register(request);
            isNewUser = true;
        }

        // 生成JWT Token
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        // 构建TokenResponse
        TokenResponse tokenResponse = new TokenResponse(
                accessToken,
                refreshToken,
                jwtUtil.getRemainingTime(accessToken),
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        // 设置是否为新用户标识
        tokenResponse.setIsNewUser(isNewUser);

        return tokenResponse;
    }

    @Override
    public User login(User user, String password) {
        //简单密码验证
        // TODO：这里先用临时设定的简单明文密码，后续可用BCrypt加密
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
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
    @Transactional
    public User register(AuthRequest request) {
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

    @Override
    public TokenResponse loginByEmail(String email, String password) {
        //根据邮箱查询用户
        User user = userMapper.findByEmail(email);

        //验证密码
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            // 检查用户状态
            if (user.getStatus() != null && user.getStatus() == 0) {
                throw new RuntimeException("用户已被禁用");
            }

            // 生成JWT Token
            String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

            // 构建TokenResponse
            return new TokenResponse(
                    accessToken,
                    refreshToken,
                    jwtUtil.getRemainingTime(accessToken),
                    user.getId(),
                    user.getUsername(),
                    user.getRole()
            );
        }
        throw new RuntimeException("邮箱或密码错误");
    }
}
