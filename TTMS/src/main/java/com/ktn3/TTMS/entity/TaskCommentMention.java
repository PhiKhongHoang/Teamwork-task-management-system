package com.ktn3.TTMS.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_comment_mentions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCommentMention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Gắn thẻ ở comment nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private TaskComment comment;

    // Gắn thẻ ai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
