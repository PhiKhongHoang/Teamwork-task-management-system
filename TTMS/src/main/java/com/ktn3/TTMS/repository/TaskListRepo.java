package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.TaskList;
import com.ktn3.TTMS.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskListRepo extends JpaRepository<TaskList, Long> {
    List<TaskList> findByProject(Project project);
}
