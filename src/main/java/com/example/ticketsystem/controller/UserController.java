package com.example.ticketsystem.controller;

import com.example.ticketsystem.dto.*;
import com.example.ticketsystem.entity.Address;
import com.example.ticketsystem.entity.User;
import com.example.ticketsystem.mapper.AddressMapper;
import com.example.ticketsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lark
 * @ date 2025/12/12  16:52
 * @ description
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressMapper addressMapper;

    //提取Token中的用户ID
    private Long extractUserIdFromToken(String authHeader) {
        //检查Authorization头
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        //提取token
        String token = authHeader.substring(7).trim();
        //解析token:"user_{userId}_{timestamp}"
        if (token.startsWith("user_")) {
            try {
                //分割字符串：["user", "1", "1741812345678"]
                String[] parts = token.split("_");
                if (parts.length >= 2) {
                    //第二部分就是userId
                    return Long.parseLong(parts[1]);
                }
            } catch (NumberFormatException e) {
                //如果第二部分不是数字，返回null
                System.out.println("Token格式错误，无法解析userId: " + token);
                return null;
            }
        }
        System.out.println("无法识别的Token格式: " + token);
        return null;
    }

    // ======== 用户信息相关接口 ========

    //获取当前用户信息
    @GetMapping("/info")
    public ApiResponse<Object> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        UserInfoDTO userInfo = UserInfoDTO.fromUser(user);
        return ApiResponse.success(userInfo);
    }

    //修改个人信息
    @PutMapping("/info")
    public ApiResponse<Object> updateUserInfo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserUpdateRequest request) {

        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        //更新字段（用户信息），只修改需要修改的部分
        // TODO：注意当前代码无法允许用户把字段设置为空，后续要改
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

        // TODO: 实际应该执行数据库更新
        // userMapper.update(user);

        UserInfoDTO userInfo = UserInfoDTO.fromUser(user);
        return ApiResponse.success("更新成功", userInfo);
    }

    // ======== 收货地址相关接口 ========

    //获取收货地址列表
    @GetMapping("/addresses")
    public ApiResponse<Object> getAddresses(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        List<Address> addresses = addressMapper.findByUserId(userId);
        List<AddressDTO> addressDTOs = addresses.stream()
                .map(AddressDTO::fromEntity)
                .collect(Collectors.toList());

        return ApiResponse.success(addressDTOs);
    }

    //添加收货地址
    @PostMapping("/address")
    public ApiResponse<Object> addAddress(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AddressRequest request) {

        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        //如果要设置为默认地址，先取消其他默认地址（确保默认地址只有一个）
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            //把当前登录用户名下所以现存地址的默认标记设为0
            addressMapper.cancelAllDefault(userId);
        }

        //将零散request组装成完整Address实体对象

        Address address = new Address();
        //此处userId由token中解析
        address.setUserId(userId);
        //抽取并设置数据
        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setAddress(request.getAddress());
        //设置新地址的状态
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : 0);

        addressMapper.insert(address);

        return ApiResponse.success("添加成功", AddressDTO.fromEntity(address));
    }

    //修改收货地址
    @PutMapping("/address/{id}")
    public ApiResponse<Object> updateAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody AddressRequest request) {

        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        //检查地址是否存在且属于当前用户
        Address address = addressMapper.findById(id);
        if (address == null) {
            return ApiResponse.error(404, "地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            return ApiResponse.error(403, "无权修改此地址");
        }

        //如果要设置为默认地址，先取消其他默认地址
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            addressMapper.cancelAllDefault(userId);
        }

        //更新字段
        address.setName(request.getName());
        address.setPhone(request.getPhone());
        address.setAddress(request.getAddress());
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }

        addressMapper.update(address);

        return ApiResponse.success("修改成功", AddressDTO.fromEntity(address));
    }

    //删除收货地址
    @DeleteMapping("/address/{id}")
    public ApiResponse<Object> deleteAddress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long userId = extractUserIdFromToken(authHeader);
        if (userId == null) {
            return ApiResponse.error(401, "未授权");
        }

        //检查地址是否存在且属于当前用户
        Address address = addressMapper.findById(id);
        if (address == null) {
            return ApiResponse.error(404, "地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            return ApiResponse.error(403, "无权删除此地址");
        }

        addressMapper.delete(id);

        return ApiResponse.success("删除成功");
    }
}
