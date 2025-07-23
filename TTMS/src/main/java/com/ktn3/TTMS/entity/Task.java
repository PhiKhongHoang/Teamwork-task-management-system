package com.ktn3.TTMS.entity;

import com.ktn3.TTMS.constant.TaskPriority;
import com.ktn3.TTMS.constant.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Nếu muốn Kanban board, thì task sẽ thuộc 1 cột (list)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private TaskList list; // nullable, nếu không có board

    @NotBlank
    @Column(nullable = false, length = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    // Người tạo task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Người được giao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskPriority priority; // HIGH, MEDIUM, LOW

    // Nếu muốn không gửi trùng nhắc nhở, bạn nên thêm trường:
    private boolean remindedBeforeDeadline; // default false

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status; // TODO, IN_PROGRESS, REVIEW, DONE

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = TaskStatus.TODO;
        if (this.priority == null) this.priority = TaskPriority.MEDIUM;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

