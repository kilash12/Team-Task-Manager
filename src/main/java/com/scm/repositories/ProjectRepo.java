package com.scm.repositories;

import com.scm.entities.Project;
import com.scm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, String> {
    List<Project> findByCreatedBy(User admin);
    List<Project> findByTeamMembersContaining(User member);
}