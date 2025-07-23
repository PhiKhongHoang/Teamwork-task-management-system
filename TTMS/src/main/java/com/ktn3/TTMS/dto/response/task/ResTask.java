package com.ktn3.TTMS.dto.response.task;

import com.ktn3.TTMS.constant.TaskPriority;
import com.ktn3.TTMS.constant.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResTask {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private Long assigneeId;
    private String assigneeName;
    private Long projectId;
    private Long listId;
}

