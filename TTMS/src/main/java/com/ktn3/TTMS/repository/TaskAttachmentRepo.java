package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.TaskAttachment;
import com.ktn3.TTMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAttachmentRepo extends JpaRepository<TaskAttachment, Long> {
    List<TaskAttachment> findByTask(Task task);
}
