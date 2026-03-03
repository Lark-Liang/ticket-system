package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminUserDetailDTO;
import com.example.ticketsystem.dto.admin.AdminUserListDTO;
import com.example.ticketsystem.dto.admin.AdminUserQueryDTO;
import com.example.ticketsystem.dto.admin.AdminUserUpdateRequest;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.UserMapper;
import com.example.ticketsystem.service.AdminUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Lark
 * @ date 2026/3/2  20:17
 * @ description 管理员用户服务实现类
 */
@Service
public class AdminUserServiceImpl implements AdminUserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public ListResponseDTO<AdminUserListDTO> getUserList(AdminUserQueryDTO queryDTO) {
        // 参数处理
        if (queryDTO.getPage() < 1) queryDTO.setPage(1);
        if (queryDTO.getSize() < 1 || queryDTO.getSize() > 100) queryDTO.setSize(10);

        // 使用 PageHelper 开始分页
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getSize());

        // 执行查询
        List<User> users = userMapper.findByConditions(
                queryDTO.getUsername(),
                queryDTO.getPhone(),
                queryDTO.getEmail(),
                queryDTO.getStatus(),
                queryDTO.getRole(),
                queryDTO.getStartDate(),
                queryDTO.getEndDate()
        );

        // 获取分页信息
        PageInfo<User> pageInfo = new PageInfo<>(users);

        // 转换为 DTO
        List<AdminUserListDTO> userList = users.stream()
                .map(AdminUserListDTO::fromUser)
                .collect(Collectors.toList());

        return ListResponseDTO.of(
                userList,
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
    }

    @Override
    public AdminUserDetailDTO getUserDetail(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return AdminUserDetailDTO.fromUser(user);
    }

    @Override
    @Transactional
    public AdminUserDetailDTO updateUser(Long userId, AdminUserUpdateRequest updateRequest) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        // 查询用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新字段（只更新非空字段）
        if (updateRequest.getNickname() != null) {
            user.setNickname(updateRequest.getNickname());
        }
        if (updateRequest.getBio() != null) {
            user.setBio(updateRequest.getBio());
        }
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }
        if (updateRequest.getBirthday() != null && !updateRequest.getBirthday().isEmpty()) {
            user.setBirthday(LocalDate.parse(updateRequest.getBirthday(), DateTimeFormatter.ISO_DATE));
        }
        if (updateRequest.getBackgroundImage() != null) {
            user.setBackgroundImage(updateRequest.getBackgroundImage());
        }
        if (updateRequest.getPhone() != null) {
            user.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getAvatar() != null) {
            user.setAvatar(updateRequest.getAvatar());
        }
        if (updateRequest.getStatus() != null) {
            user.setStatus(updateRequest.getStatus());
        }

        // 执行更新
        int rows = userMapper.update(user);
        if (rows == 0) {
            throw new RuntimeException("更新用户信息失败");
        }

        return AdminUserDetailDTO.fromUser(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("状态值不正确（0-禁用，1-启用）");
        }

        // 查询用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 不能禁用自己
        Long currentAdminId = com.example.ticketsystem.util.RequestHolder.getUserId();
        if (userId.equals(currentAdminId) && status == 0) {
            throw new RuntimeException("不能禁用当前登录的管理员账号");
        }

        int rows = userMapper.updateStatus(userId, status);
        if (rows == 0) {
            throw new RuntimeException("更新用户状态失败");
        }
    }
}
