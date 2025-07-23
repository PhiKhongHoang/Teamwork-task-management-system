package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.dto.response.calendar.ResCalendarTask;
import com.ktn3.TTMS.entity.Task;
import com.ktn3.TTMS.entity.User;
import com.ktn3.TTMS.constant.TaskStatus;
import com.ktn3.TTMS.repository.TaskRepo;
import com.ktn3.TTMS.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final TaskRepo taskRepo;

    // Lấy toàn bộ task (còn hạn) của user dạng calendar
    @Override
    public ResCommonApi<?> getMyCalendarTasks(User user, LocalDate from, LocalDate to) {
        if (user == null) return ResCommonApi.error("User không hợp lệ!", 400);

        // Mặc định lấy 1 tháng nếu ko truyền from/to
        if (from == null) from = LocalDate.now().withDayOfMonth(1);
        if (to == null) to = LocalDate.now().plusMonths(1);

        List<Task> tasks = taskRepo.findTasksByAssigneeAndDeadlineBetween(user, from, to);

        List<ResCalendarTask> res = tasks.stream().map(t ->
                ResCalendarTask.builder()
                        .id(t.getId())
                        .title(t.getTitle())
                        .deadline(t.getDeadline())
                        .priority(t.getPriority())
                        .status(t.getStatus())
                        .projectId(t.getProject().getId())
                        .projectName(t.getProject().getName())
                        .build()
        ).collect(Collectors.toList());

        return ResCommonApi.success(res, "Lấy danh sách task theo lịch thành công!");
    }
}
