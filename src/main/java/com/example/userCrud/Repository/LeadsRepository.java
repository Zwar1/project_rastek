package com.example.userCrud.Repository;

import com.example.userCrud.Entity.LeadsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadsRepository extends JpaRepository<LeadsEntity,Long> {

    boolean existsByClientName(String ClientName);

    Optional<LeadsEntity> findFirstById(Long id);
}
