package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.TaskComment;
import com.ktn3.TTMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepo extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTaskOrderByCreatedAtAsc(Task task);
}
