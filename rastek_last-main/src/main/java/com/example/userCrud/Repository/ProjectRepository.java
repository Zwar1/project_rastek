package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CVEntity;
import com.example.userCrud.Entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findAllByOrderByProjectEndAsc();

    List<ProjectEntity> findAllByOrderByProjectEndDesc();

    List<ProjectEntity> findAllByOrderByPriorityAsc();

    List<ProjectEntity> findAllByOrderByPriorityDesc();
}
