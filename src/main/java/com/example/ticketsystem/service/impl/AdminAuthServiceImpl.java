package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.TokenResponse;
import com.example.ticketsystem.dto.admin.AdminLoginRequest;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.UserMapper;
import com.example.ticketsystem.service.AdminAuthService;
import com.example.ticketsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lark
 * @ date 2026/3/2  18:16
 * @ description 管理员认证服务实现类
 */
@Service
public class AdminAuthServiceImpl implements AdminAuthService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public TokenResponse login(AdminLoginRequest request) {
        // 参数校验
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        // 查询用户
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        // TODO: 后续改为BCrypt加密
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查是否为管理员
        if (!"admin".equals(user.getRole())) {
            throw new RuntimeException("无管理员权限");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("管理员账号已被禁用");
        }

        // 生成Token
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

        return new TokenResponse(
                accessToken,
                refreshToken,
                jwtUtil.getRemainingTime(accessToken),
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    @Override
    public void logout(Long adminId) {
        System.out.println("管理员退出登录，ID: " + adminId);
    }
}
