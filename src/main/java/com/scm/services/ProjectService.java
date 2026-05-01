package com.scm.services;

import com.scm.entities.Project;
import com.scm.entities.User;
import java.util.List;

public interface ProjectService {
    Project createProject(Project project);
    Project updateProject(Project project);
    void deleteProject(String id);
    Project getProjectById(String id);
    List<Project> getAllProjects();
    List<Project> getProjectsByAdmin(User admin);
    List<Project> getProjectsForMember(User member);
    void addMemberToProject(String projectId, User member);
    long countAllProjects();

}