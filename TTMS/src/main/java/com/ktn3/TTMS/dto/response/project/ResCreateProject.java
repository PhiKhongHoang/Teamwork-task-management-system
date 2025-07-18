package com.ktn3.TTMS.dto.response.project;

import com.ktn3.TTMS.constant.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResCreateProject {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private ProjectStatus status;

}
