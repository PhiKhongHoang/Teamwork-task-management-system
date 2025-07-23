package com.ktn3.TTMS.service.impl;

import com.ktn3.TTMS.entity.Task;
import com.ktn3.TTMS.entity.User;
import com.ktn3.TTMS.constant.TaskStatus;
import com.ktn3.TTMS.repository.TaskRepo;
import com.ktn3.TTMS.service.DeadlineReminderService;
import com.ktn3.TTMS.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadlineReminderServiceImpl implements DeadlineReminderService {
    private final TaskRepo taskRepo;
    private final MailService mailService;

    // Chạy mỗi 30 phút
    @Override
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void remindTaskBeforeDeadline() {
        // Lấy mốc thời gian muốn nhắc (ví dụ 24h trước deadline)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusHours(23);
        LocalDateTime end = now.plusHours(25);

        // Lấy các task sắp đến hạn, chưa DONE, chưa được remind (nếu có trường này)
        List<Task> tasks = taskRepo.findAll().stream()
                .filter(t -> t.getDeadline() != null
                                && t.getStatus() != TaskStatus.DONE
                                && t.getDeadline().atStartOfDay().isAfter(start)
                                && t.getDeadline().atStartOfDay().isBefore(end)
                                && !t.isRemindedBeforeDeadline() )
                .toList();

        for (Task task : tasks) {
            User user = task.getAssignee();
            if (user != null) {
                mailService.sendDeadlineReminder(user.getEmail(), task.getTitle(), task.getDeadline());
                // Nếu có trường isRemindedBeforeDeadline:
                 task.setRemindedBeforeDeadline(true);
                 taskRepo.save(task);
                log.info("Đã gửi nhắc nhở task: {} cho {}", task.getId(), user.getEmail());
            }
        }
    }
}
