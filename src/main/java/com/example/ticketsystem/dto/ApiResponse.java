package com.example.ticketsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static ApiResponse<Object> success(Object data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static ApiResponse<Object> success(String message, Object data) {
        return new ApiResponse<>(200, message, data);
    }

    public static ApiResponse<Object> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
