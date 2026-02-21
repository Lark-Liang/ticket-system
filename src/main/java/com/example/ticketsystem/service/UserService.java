package com.example.ticketsystem.service;

import com.example.ticketsystem.dto.AddressRequest;
import com.example.ticketsystem.dto.UserInfoDTO;
import com.example.ticketsystem.dto.UserUpdateRequest;
import com.example.ticketsystem.entity.Address;
import com.example.ticketsystem.entity.User;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/2/13  17:36
 * @ description 用户服务接口
 */
public interface UserService {
    //根据用户ID查询用户信息
    User findById(Long userId);

    //获取用户信息DTO
    UserInfoDTO getUserInfo(Long userId);

    //更新用户信息
    UserInfoDTO updateUserInfo(Long userId, UserUpdateRequest request);

    //获取用户地址列表
    List<Address> getUserAddresses(Long userId);

    //添加地址
    Address addAddress(Long userId, AddressRequest request);

    //更新地址
    Address updateAddress(Long userId, Long addressId, AddressRequest request);

    //删除地址
    void deleteAddress(Long userId, Long addressId);
}
