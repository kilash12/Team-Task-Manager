package com.scm.repositories;

import com.scm.entities.Task;
import com.scm.entities.Project;
import com.scm.entities.User;
import com.scm.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TaskRepo extends JpaRepository<Task, String> {
    List<Task> findByAssignedTo(User user);
    List<Task> findByProject(Project project);
    long countByStatus(TaskStatus status);
    long countByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);
}