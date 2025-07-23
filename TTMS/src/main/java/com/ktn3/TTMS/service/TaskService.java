package com.ktn3.TTMS.service;

import com.ktn3.TTMS.constant.TaskStatus;
import com.ktn3.TTMS.dto.request.task.ReqCreateTask;
import com.ktn3.TTMS.dto.request.task.ReqUpdateTask;
import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface TaskService {
    ResCommonApi<?> createTask(ReqCreateTask req, User actor);
    ResCommonApi<?> updateTask(ReqUpdateTask req, User actor);
    ResCommonApi<?> changeTaskStatus(Long taskId, TaskStatus newStatus, User actor);
    ResCommonApi<?> uploadTaskAttachment(Long taskId, MultipartFile file, User actor);
    ResCommonApi<?> searchTasks(Long projectId, TaskStatus status, Long assigneeId, LocalDate deadline, Pageable pageable, User actor);
}
