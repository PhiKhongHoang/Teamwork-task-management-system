package com.ktn3.TTMS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReqCreateComment {
    @NotNull
    private Long taskId;

    @NotBlank
    private String content;

    // List id user được mention (FE tự tách @tag)
    private List<Long> mentionUserIds;
}
