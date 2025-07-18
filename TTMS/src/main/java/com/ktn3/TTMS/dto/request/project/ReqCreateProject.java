package com.ktn3.TTMS.dto.request.project;

import com.ktn3.TTMS.constant.ProjectStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqCreateProject {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    @Column
    private LocalDate deadline;

    // **Cho phép null** => project cá nhân
    private Long idTeam;

}
