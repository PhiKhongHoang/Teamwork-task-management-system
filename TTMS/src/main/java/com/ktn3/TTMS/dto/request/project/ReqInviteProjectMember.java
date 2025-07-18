package com.ktn3.TTMS.dto.request.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReqInviteProjectMember {
    @NotBlank
    private String email;
    @NotNull
    private Long roleId; // role project (MEMBER, GUEST, ...)
}

