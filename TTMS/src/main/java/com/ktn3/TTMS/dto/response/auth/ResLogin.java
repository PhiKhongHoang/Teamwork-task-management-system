package com.ktn3.TTMS.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResLogin {
    private Long id;
    private String email;
    private String avatarUrl;
    private String name;
    private String accessToken; // JWT trả về cho client
    private String refreshToken;
}
