package com.scm.services;

import com.scm.entities.Task;
import com.scm.entities.TaskStatus;
import com.scm.entities.User;
import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Task task);
    void deleteTask(String id);
    Task getTaskById(String id);
    List<Task> getAllTasks();
    List<Task> getTasksAssignedToUser(User user);
    List<Task> getTasksByProject(String projectId);
    long countTasksByStatus(TaskStatus status);
    long countOverdueTasks();
    long countAllTasks();
}