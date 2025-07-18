package com.ktn3.TTMS.dto.response.user;

import com.ktn3.TTMS.constant.GenderEnum;
import com.ktn3.TTMS.dto.response.role.ResRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResUser {
    private Long id;
    private String email;
    private String name;
    private String avatarUrl;
    private String phone;
    private GenderEnum gender;
    private List<ResRole> roles = new ArrayList<>();
}
