package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.TaskCommentAttachment;
import com.ktn3.TTMS.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentAttachmentRepo extends JpaRepository<TaskCommentAttachment, Long> {
    List<TaskCommentAttachment> findByComment(TaskComment comment);
}
