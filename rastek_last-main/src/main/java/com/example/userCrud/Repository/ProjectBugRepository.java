package com.example.userCrud.Repository;

import com.example.userCrud.Entity.ProjectBugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectBugRepository extends JpaRepository<ProjectBugEntity, Long> {
    List<ProjectBugEntity> findByProjectId(Long projectId);
    Optional<ProjectBugEntity> findByIdAndProjectId(Long id, Long projectId);
}