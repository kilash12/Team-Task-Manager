package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.services.ProjectService;
import com.scm.services.TaskService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        long totalUsers = userService.countAllUsers();
        long totalProjects = projectService.countAllProjects();  // ✅ fixed
        long totalTasks = taskService.countAllTasks();
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalTasks", totalTasks);
        return "user/admin_dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/admin_users";
    }

    @GetMapping("/projects")
    public String manageProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "user/admin_projects";
    }

    @GetMapping("/tasks")
    public String manageTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "user/admin_tasks";
    }
}