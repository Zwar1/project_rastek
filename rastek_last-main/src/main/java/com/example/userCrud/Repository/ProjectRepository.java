package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CVEntity;
import com.example.userCrud.Entity.EmployeeEntity;
import com.example.userCrud.Entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findAllByOrderByProjectEndAsc();

    List<ProjectEntity> findAllByOrderByProjectEndDesc();

    List<ProjectEntity> findAllByOrderByPriorityAsc();

    List<ProjectEntity> findAllByOrderByPriorityDesc();

    @Query("SELECT p.member FROM ProjectEntity p WHERE p.id = :id")
    Set<EmployeeEntity> findMembersByProjectId(@Param("id") Long id);
}
