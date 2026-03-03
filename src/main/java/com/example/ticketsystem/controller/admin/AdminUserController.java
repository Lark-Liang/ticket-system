package com.example.ticketsystem.controller.admin;

import com.example.ticketsystem.dto.ApiResponse;
import com.example.ticketsystem.dto.ListResponseDTO;
import com.example.ticketsystem.dto.admin.AdminUserDetailDTO;
import com.example.ticketsystem.dto.admin.AdminUserListDTO;
import com.example.ticketsystem.dto.admin.AdminUserQueryDTO;
import com.example.ticketsystem.dto.admin.AdminUserUpdateRequest;
import com.example.ticketsystem.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lark
 * @ date 2026/3/2  20:26
 * @ description 管理员用户管理控制器
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public ApiResponse<?> getUserList(AdminUserQueryDTO queryDTO) {
        try {
            ListResponseDTO<AdminUserListDTO> result = adminUserService.getUserList(queryDTO);
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getUserDetail(@PathVariable Long id) {
        try {
            AdminUserDetailDTO userDetail = adminUserService.getUserDetail(id);
            return ApiResponse.success(userDetail);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/{id}")
    public ApiResponse<?> updateUser(
            @PathVariable Long id,
            @RequestBody AdminUserUpdateRequest updateRequest) {
        try {
            AdminUserDetailDTO userDetail = adminUserService.updateUser(id, updateRequest);
            return ApiResponse.success("修改成功", userDetail);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public ApiResponse<?> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        try {
            adminUserService.updateUserStatus(id, status);
            String message = status == 1 ? "用户已启用" : "用户已禁用";
            return ApiResponse.success(message);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
