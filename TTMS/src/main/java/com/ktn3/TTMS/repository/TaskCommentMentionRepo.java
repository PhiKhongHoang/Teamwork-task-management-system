package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.TaskCommentMention;
import com.ktn3.TTMS.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentMentionRepo extends JpaRepository<TaskCommentMention, Long> {
    List<TaskCommentMention> findByComment(TaskComment comment);
}
