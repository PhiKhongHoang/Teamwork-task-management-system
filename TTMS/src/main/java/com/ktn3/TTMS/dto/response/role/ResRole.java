package com.ktn3.TTMS.dto.response.role;

import com.ktn3.TTMS.constant.RoleScope;
import com.ktn3.TTMS.dto.response.permission.ResPermission;
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
public class ResRole {
    private Long id;
    private String name;
    private RoleScope scope;
    private List<ResPermission> permissions = new ArrayList<>();
}
