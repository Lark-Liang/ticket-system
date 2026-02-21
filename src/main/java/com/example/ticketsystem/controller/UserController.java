package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.*;
import com.example.ticketsystem.entity.Address;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.service.UserService;
import com.example.ticketsystem.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lark
 * @ date 2025/12/12  16:52
 * @ description 用户模块，含获取/修改用户信息、对收货地址的CRUD
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ======== 用户信息相关接口 ========

    /**获取当前用户信息
     * GET /user/info
     */
    @GetMapping("/info")
    public ApiResponse<Object> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        try {
            UserInfoDTO userInfo = userService.getUserInfo(userId);
            return ApiResponse.success(userInfo);
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }

    /**修改个人信息
     * GET /user/info
     */
    @PutMapping("/info")
    public ApiResponse<Object> updateUserInfo(
            HttpServletRequest request,
            @RequestBody UserUpdateRequest userUpdateRequest) {

        Long userId = (Long) request.getAttribute("userId");

        // TODO：注意当前代码无法允许用户把字段设置为空，后续要改
        try {
            UserInfoDTO userInfo = userService.updateUserInfo(userId, userUpdateRequest);
            return ApiResponse.success("更新成功", userInfo);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // ======== 收货地址相关接口 ========

    /**获取收货地址列表
     * GET /user/addresses
     */
    @GetMapping("/addresses")
    public ApiResponse<Object> getAddresses(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        // TODO：如果这里地址查出来是空，会报错空指针
        try {
            List<Address> addresses = userService.getUserAddresses(userId);
            List<AddressDTO> addressDTOs = addresses.stream()
                    .map(AddressDTO::fromEntity)
                    .collect(Collectors.toList());
            return ApiResponse.success(addressDTOs);
        } catch (RuntimeException e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }

    /**添加收货地址
     * POST /user/address
     */
    @PostMapping("/address")
    @Transactional
    public ApiResponse<Object> addAddress(
            HttpServletRequest request,
            @RequestBody AddressRequest addressRequest) {

        Long userId = (Long) request.getAttribute("userId");

        try {
            Address address = userService.addAddress(userId, addressRequest);
            return ApiResponse.success("添加成功", AddressDTO.fromEntity(address));
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**修改收货地址
     * PUT /user/address/{id}
     */
    @PutMapping("/address/{id}")
    @Transactional
    public ApiResponse<Object> updateAddress(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody AddressRequest addressRequest) {

        Long userId = (Long) request.getAttribute("userId");

        try {
            Address address = userService.updateAddress(userId, id, addressRequest);
            return ApiResponse.success("修改成功", AddressDTO.fromEntity(address));
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**删除收货地址
     * DELETE /user/address/{id}
     */
    @DeleteMapping("/address/{id}")
    @Transactional
    public ApiResponse<Object> deleteAddress(
            HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = (Long) request.getAttribute("userId");

        try {
            userService.deleteAddress(userId, id);
            return ApiResponse.success("删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
