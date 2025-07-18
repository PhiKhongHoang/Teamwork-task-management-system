package com.ktn3.TTMS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResCommonApi<T> {
    private boolean success;
    private String message;
    private Integer statusCode;
    private T data;

    public static <T> ResCommonApi<T> success(T data, String message) {
        return ResCommonApi.<T>builder()
                .success(true)
                .message(message)
                .statusCode(null) // để Advice tự động gán nếu không chỉ định
                .data(data)
                .build();
    }

    public static <T> ResCommonApi<T> error(String message, Integer statusCode) {
        return ResCommonApi.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .data(null)
                .build();
    }
}
