package com.ktn3.TTMS.dto.request.user;

import com.ktn3.TTMS.constant.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqCreateUser {
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100) // Chỉ cần validate password raw, sau đó hash bằng BCrypt sẽ được 60 ký tự!
    private String password;

    @Size(max = 255)
    private String avatarUrl;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 15)
    @Pattern(regexp = "^\\d{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotNull // Nếu muốn bắt buộc giới tính
    private GenderEnum gender;
}
