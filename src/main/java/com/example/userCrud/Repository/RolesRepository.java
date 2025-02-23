package com.example.userCrud.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userCrud.Entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer>{
    boolean existsByName(String name);

    Optional<Roles> findFirstById(Long id);
}
