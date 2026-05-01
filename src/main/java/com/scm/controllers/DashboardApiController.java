package com.scm.controllers;

import com.scm.entities.TaskStatus;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.TaskService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    @Autowired private TaskService taskService;
    @Autowired private UserService userService;

    @GetMapping
    public Map<String, Object> getStats(Authentication auth) {
        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(auth));
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", taskService.getTasksAssignedToUser(user).size());
        stats.put("completedTasks", taskService.countTasksByStatus(TaskStatus.DONE));
        stats.put("pendingTasks", taskService.countTasksByStatus(TaskStatus.TODO) +
                taskService.countTasksByStatus(TaskStatus.IN_PROGRESS));
        stats.put("overdueTasks", taskService.countOverdueTasks());
        return stats;
    }
}