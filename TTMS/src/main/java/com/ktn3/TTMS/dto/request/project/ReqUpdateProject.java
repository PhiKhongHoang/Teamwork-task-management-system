package com.ktn3.TTMS.dto.request.project;

import com.ktn3.TTMS.constant.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReqUpdateProject {

    @NotBlank(message = "Tên project không được để trống")
    @Size(max = 100, message = "Tên project tối đa 100 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả project tối đa 255 ký tự")
    private String description;

    private LocalDate deadline;

    private ProjectStatus status; // Ví dụ: NEW, IN_PROGRESS, COMPLETED, CANCELLED
}
