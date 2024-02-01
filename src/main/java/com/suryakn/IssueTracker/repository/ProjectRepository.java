package com.suryakn.IssueTracker.repository;

import com.suryakn.IssueTracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
