package com.ktn3.TTMS.dto.request.task;

import com.ktn3.TTMS.constant.TaskPriority;
import com.ktn3.TTMS.constant.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReqUpdateTask {
    @NotNull
    private Long id; // Id task

    private String title;
    private String description;
    private LocalDate deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private Long assigneeId;
    private Long listId;
}

