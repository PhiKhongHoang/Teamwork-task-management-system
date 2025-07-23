package com.ktn3.TTMS.dto.request.task;

import com.ktn3.TTMS.constant.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReqCreateTask {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long projectId;

    private Long assigneeId; // Có thể null

    private LocalDate deadline;

    private TaskPriority priority;

    // Nếu có board/list thì truyền luôn listId:
    private Long listId;
}

