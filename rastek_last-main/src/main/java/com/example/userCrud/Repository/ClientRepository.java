package com.example.userCrud.Repository;

import com.example.userCrud.Entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    boolean existsByClientName(String ClientName);

    ClientEntity findByClientName(String clientName);
}
