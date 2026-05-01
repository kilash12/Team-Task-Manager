package com.scm.controllers;

import com.scm.entities.Project;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ProjectService;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Controller
@RequestMapping("/user/projects")
public class ProjectViewController {

    @Autowired private ProjectService projectService;
    @Autowired private UserService userService;

    @GetMapping
    public String listProjects(Model model, Authentication auth) {
        User current = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(auth));
        if (current.getRoleList().contains("ROLE_ADMIN")) {
            model.addAttribute("projects", projectService.getProjectsByAdmin(current));
        } else {
            model.addAttribute("projects", projectService.getProjectsForMember(current));
        }
        return "user/projects";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        return "user/create_project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project, Authentication auth) {
        User admin = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(auth));
        project.setId(UUID.randomUUID().toString());
        project.setCreatedBy(admin);
        projectService.createProject(project);
        return "redirect:/user/projects";
    }

    @GetMapping("/{id}/add-member")
    public String showAddMemberForm(@PathVariable String id, Model model) {
        model.addAttribute("projectId", id);
        model.addAttribute("users", userService.getAllUsers());
        return "user/add_member";
    }

    @PostMapping("/{id}/add-member")
    public String addMember(@PathVariable String id, @RequestParam String userId) {
        User member = userService.getUserById(userId).orElse(null);
        if (member != null) {
            projectService.addMemberToProject(id, member);
        }
        return "redirect:/user/projects";
    }
}