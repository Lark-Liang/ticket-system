package com.example.ticketsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lark
 * @ date 2025/12/23  20:46
 * @ description token响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;    //过期时间（秒）
    private String tokenType;
    private Long userId;
    private String username;
    private String role;
    private Boolean isNewUser;

    public TokenResponse(String accessToken, String refreshToken, Long expiresIn, Long userId, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.isNewUser = false;
    }

    //只返回Access Token的构造方法（兼容旧版）
    public static TokenResponse of(String accessToken, Long expiresIn, Long userId, String username, String role) {
        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setExpiresIn(expiresIn);
        response.setTokenType("Bearer");
        response.setUserId(userId);
        response.setUsername(username);
        response.setRole(role);
        return response;
    }
}
