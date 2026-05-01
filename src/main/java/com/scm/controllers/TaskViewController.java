package com.scm.controllers;

import com.scm.entities.Task;
import com.scm.entities.TaskStatus;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ProjectService;
import com.scm.services.TaskService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/user/tasks")
public class TaskViewController {

    @Autowired private TaskService taskService;
    @Autowired private ProjectService projectService;
    @Autowired private UserService userService;

    @GetMapping
    public String listTasks(Model model, Authentication auth) {
        User current = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(auth));
        model.addAttribute("tasks", taskService.getTasksAssignedToUser(current));
        return "user/tasks";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Authentication auth) {
        User current = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(auth));
        model.addAttribute("task", new Task());
        model.addAttribute("projects", projectService.getProjectsByAdmin(current));
        model.addAttribute("members", current.getRoleList().contains("ROLE_ADMIN") ? userService.getAllUsers() : null);
        return "user/create_task";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, @RequestParam String assignedToId,
                             Authentication auth) {
        User current = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(auth));
        task.setId(UUID.randomUUID().toString());
        task.setAssignedTo(userService.getUserById(assignedToId).orElse(null));
        taskService.createTask(task);
        return "redirect:/user/tasks";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable String id, @RequestParam TaskStatus status) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            task.setStatus(status);
            taskService.updateTask(task);
        }
        return "redirect:/user/tasks";
    }
}