package com.scm.services.impl;

import com.scm.entities.Task;
import com.scm.entities.TaskStatus;
import com.scm.entities.User;
import com.scm.repositories.TaskRepo;
import com.scm.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired private TaskRepo taskRepo;

    @Override
    public Task createTask(Task task) {
        if (task.getId() == null) {
            task.setId(UUID.randomUUID().toString());
        }
        return taskRepo.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepo.save(task);
    }

    @Override
    public void deleteTask(String id) {
        taskRepo.deleteById(id);
    }

    @Override
    public Task getTaskById(String id) {
        return taskRepo.findById(id).orElse(null);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public List<Task> getTasksAssignedToUser(User user) {
        return taskRepo.findByAssignedTo(user);
    }

    @Override
    public List<Task> getTasksByProject(String projectId) {
        // need to fetch project first, but for simplicity we can use a custom query
        // we'll skip for brevity – can be added later
        return List.of();
    }

    @Override
    public long countTasksByStatus(TaskStatus status) {
        return taskRepo.countByStatus(status);
    }

    @Override
    public long countOverdueTasks() {
        return taskRepo.countByDueDateBeforeAndStatusNot(LocalDate.now(), TaskStatus.DONE);
    }

    @Override
    public long countAllTasks() {
        return taskRepo.count();
    }
}