package com.scm.services.impl;

import com.scm.entities.Project;
import com.scm.entities.User;
import com.scm.repositories.ProjectRepo;
import com.scm.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired private ProjectRepo projectRepo;

    @Override
    public Project createProject(Project project) {
        if (project.getId() == null) {
            project.setId(UUID.randomUUID().toString());
        }
        return projectRepo.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        return projectRepo.save(project);
    }

    @Override
    public void deleteProject(String id) {
        projectRepo.deleteById(id);
    }

    @Override
    public Project getProjectById(String id) {
        return projectRepo.findById(id).orElse(null);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    @Override
    public List<Project> getProjectsByAdmin(User admin) {
        return projectRepo.findByCreatedBy(admin);
    }

    @Override
    public List<Project> getProjectsForMember(User member) {
        return projectRepo.findByTeamMembersContaining(member);
    }

    @Override
    public void addMemberToProject(String projectId, User member) {
        Project project = getProjectById(projectId);
        if (project != null && !project.getTeamMembers().contains(member)) {
            project.getTeamMembers().add(member);
            projectRepo.save(project);
        }
    }

    @Override
    public long countAllProjects() {
        return projectRepo.count();
    }
}