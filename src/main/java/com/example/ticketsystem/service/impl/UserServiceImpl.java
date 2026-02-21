package com.example.ticketsystem.service.impl;

import com.example.ticketsystem.dto.AddressRequest;
import com.example.ticketsystem.dto.UserInfoDTO;
import com.example.ticketsystem.dto.UserUpdateRequest;
import com.example.ticketsystem.entity.Address;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.AddressMapper;
import com.example.ticketsystem.mapper.UserMapper;
import com.example.ticketsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/2/13  17:39
 * @ description 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public User findById(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        User user = findById(userId);
        return UserInfoDTO.fromUser(user);
    }

    @Override
    @Transactional
    public UserInfoDTO updateUserInfo(Long userId, UserUpdateRequest request) {
        User user = findById(userId);

        // 只更新非空字段
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        // TODO: 执行数据库更新
        // userMapper.update(user);

        return UserInfoDTO.fromUser(user);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        findById(userId);
        List<Address> addresses = addressMapper.findByUserId(userId);
        return addresses != null ? addresses : List.of(); // 防止空指针
    }

    @Override
    @Transactional
    public Address addAddress(Long userId, AddressRequest request) {
        findById(userId);

        // 如果要设置为默认地址，先取消其他默认地址
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(userId);
        }

        // 创建新地址
        Address address = new Address();
        address.setUserId(userId);
        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setAddress(request.getAddress());
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : 0);

        addressMapper.insert(address);
        return address;
    }

    @Override
    @Transactional
    public Address updateAddress(Long userId, Long addressId, AddressRequest request) {
        findById(userId);

        // 检查地址是否存在且属于当前用户
        Address address = addressMapper.findById(addressId);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改此地址");
        }

        // 如果要设置为默认地址，先取消其他默认地址
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(userId);
        }

        // 更新字段
        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setAddress(request.getAddress());
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }

        addressMapper.update(address);
        return address;
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        findById(userId);

        // 检查地址是否存在且属于当前用户
        Address address = addressMapper.findById(addressId);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此地址");
        }

        addressMapper.delete(addressId);
    }
}
