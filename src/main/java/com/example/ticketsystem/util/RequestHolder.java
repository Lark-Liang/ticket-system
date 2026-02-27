package com.example.ticketsystem.util;

import com.example.ticketsystem.entity.User;

/**
 * @author Lark
 * @ date 2026/2/26  20:47
 * @ description 请求上下文持有类，使用ThreadLocal存储当前请求的用户信息
 */
public class RequestHolder {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userRoleHolder = new ThreadLocal<>();
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    /**
     * 设置完整的用户信息
     */
    public static void setUser(User user) {
        userHolder.set(user);
        if (user != null) {
            userIdHolder.set(user.getId());
            userRoleHolder.set(user.getRole());
        }
    }

    /**
     * 仅设置用户ID和角色（从Token解析时使用）
     */
    public static void setUserInfo(Long userId, String role) {
        userIdHolder.set(userId);
        userRoleHolder.set(role);
    }

    /**
     * 获取完整的用户信息
     */
    public static User getUser() {
        return userHolder.get();
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        Long userId = userIdHolder.get();
        if (userId != null) {
            return userId;
        }
        User user = userHolder.get();
        return user != null ? user.getId() : null;
    }

    /**
     * 获取用户角色
     */
    public static String getUserRole() {
        String role = userRoleHolder.get();
        if (role != null) {
            return role;
        }
        User user = userHolder.get();
        return user != null ? user.getRole() : null;
    }

    /**
     * 检查当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return "admin".equals(getUserRole());
    }

    /**
     * 清理ThreadLocal中的数据（必须在请求结束后调用）
     */
    public static void remove() {
        userHolder.remove();
        userIdHolder.remove();
        userRoleHolder.remove();
    }
}
