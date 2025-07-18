package com.ktn3.TTMS.dto.response.permission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResPermission {
    private Long id;
    private String groupName;
    private String name;
    private String description;
}
