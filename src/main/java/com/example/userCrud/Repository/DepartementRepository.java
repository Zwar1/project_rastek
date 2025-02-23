package com.example.userCrud.Repository;

import com.example.userCrud.Entity.DepartementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<DepartementEntity, Long> {
    Optional<DepartementEntity> findFirstById(Long id);
}
