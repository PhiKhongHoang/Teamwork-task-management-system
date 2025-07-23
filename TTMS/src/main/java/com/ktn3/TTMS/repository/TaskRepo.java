package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.Task;
import com.ktn3.TTMS.constant.TaskStatus;
import com.ktn3.TTMS.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t " +
            "WHERE t.project.id = :projectId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId) " +
            "AND (:deadline IS NULL OR t.deadline = :deadline)")
    Page<Task> searchTasks(
            @Param("projectId") Long projectId,
            @Param("status") TaskStatus status,
            @Param("assigneeId") Long assigneeId,
            @Param("deadline") LocalDate deadline,
            Pageable pageable
    );

    // Lấy task mà user là assignee (có deadline)
    List<Task> findByAssigneeAndDeadlineIsNotNullAndStatusNot(User user, TaskStatus status);

    // Lấy task theo khoảng deadline (cho calendar)
    @Query("SELECT t FROM Task t WHERE t.assignee = :user AND t.deadline BETWEEN :start AND :end")
    List<Task> findTasksByAssigneeAndDeadlineBetween(User user, LocalDate start, LocalDate end);

}
