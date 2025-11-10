package org.example.common.api;

import java.time.Instant;

public record ApiResponse<T>(
        int status,
        String message,
        T data,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(200,"ok",data,Instant.now());
    }
    public static <T> ApiResponse<T> created(T data){
        return new ApiResponse<>(201,"created",data,Instant.now());
    }
}
