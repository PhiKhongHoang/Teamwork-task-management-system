package com.ktn3.TTMS.dto.response.calendar;

import com.ktn3.TTMS.constant.TaskPriority;
import com.ktn3.TTMS.constant.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResCalendarTask {
    private Long id;
    private String title;
    private LocalDate deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private Long projectId;
    private String projectName;
}
