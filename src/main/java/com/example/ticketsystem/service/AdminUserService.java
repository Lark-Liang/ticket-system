package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminUserDetailDTO;
import com.example.ticketsystem.dto.admin.AdminUserListDTO;
import com.example.ticketsystem.dto.admin.AdminUserQueryDTO;
import com.example.ticketsystem.dto.admin.AdminUserUpdateRequest;

/**
 * @author Lark
 * @ date 2026/3/2  20:16
 * @ description 管理员用户管理服务接口
 */
public interface AdminUserService {
    //分页查询用户列表
    ListResponseDTO<AdminUserListDTO> getUserList(AdminUserQueryDTO queryDTO);

    //查询用户详情
    AdminUserDetailDTO getUserDetail(Long userId);

    //修改用户信息
    AdminUserDetailDTO updateUser(Long userId, AdminUserUpdateRequest updateRequest);

    //启用/禁用用户
    void updateUserStatus(Long userId, Integer status);
}
