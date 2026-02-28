package com.example.ticketsystem;

import com.example.ticketsystem.util.JwtUtil;
import com.example.ticketsystem.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TicketSystemApplicationTests {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 测试JWT生成和解析
     */
    @Test
    void testJwtGeneration() {
        Long testUserId = 999L;
        String testUsername = "testuser";
        String testRole = "user";

        // 生成Token
        String accessToken = jwtUtil.generateAccessToken(testUserId, testUsername, testRole);
        String refreshToken = jwtUtil.generateRefreshToken(testUserId, testUsername);

        // 验证Token
        boolean accessTokenValid = jwtUtil.validateToken(accessToken);
        boolean refreshTokenValid = jwtUtil.validateToken(refreshToken);

        assertTrue(accessTokenValid);
        assertTrue(refreshTokenValid);

        // 解析Token
        Long parsedUserId = jwtUtil.getUserIdFromToken(accessToken);
        String parsedUsername = jwtUtil.getUsernameFromToken(accessToken);
        String parsedRole = jwtUtil.getRoleFromToken(accessToken);

        assertEquals(testUserId, parsedUserId);
        assertEquals(testUsername, parsedUsername);
        assertEquals(testRole, parsedRole);

        long remainingTime = jwtUtil.getRemainingTime(accessToken);
        assertTrue(remainingTime > 0);

        System.out.println("=== JWT生成测试通过 ===");
        System.out.println("Access Token: " + accessToken);
        System.out.println("Refresh Token: " + refreshToken);
    }

    /**
     * 测试Token工具类
     */
    @Test
    void testTokenUtil() {
        String testToken = jwtUtil.generateAccessToken(888L, "tester", "admin");
        String authHeader = "Bearer " + testToken;

        // 测试TokenUtil
        Long extractedUserId = tokenUtil.extractUserIdFromToken(authHeader);
        String extractedUsername = tokenUtil.getUsernameFromToken(authHeader);
        String extractedRole = tokenUtil.getRoleFromToken(authHeader);
        boolean tokenUtilValid = tokenUtil.validateToken(authHeader);

        assertEquals(888L, extractedUserId);
        assertEquals("tester", extractedUsername);
        assertEquals("admin", extractedRole);
        assertTrue(tokenUtilValid);

        System.out.println("=== TokenUtil测试通过 ===");
    }

    /**
     * 上下文加载测试
     */
    @Test
    void contextLoads() {
        System.out.println("Spring Boot 上下文加载成功");
    }

}
