package com.example.userCrud.Repository;


import com.example.userCrud.Entity.DivisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<DivisionEntity, Long> {
    DivisionEntity findFirstById(Long id);
}
