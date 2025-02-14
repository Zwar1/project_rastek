package com.example.userCrud.Repository;

import com.example.userCrud.Entity.CVEntity;
import com.example.userCrud.Entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
