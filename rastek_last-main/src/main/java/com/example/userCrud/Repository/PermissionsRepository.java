package com.example.userCrud.Repository;

import com.example.userCrud.Entity.PermissionsEntity;
import com.example.userCrud.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionsRepository extends JpaRepository<PermissionsEntity, Long> {
    Optional<PermissionsEntity> findByPermissionKey(String permissionKey);

    List<PermissionsEntity> findByCategory(String category);

    List<PermissionsEntity> findByAction(String action);

    boolean existsByPermissionKey(String permissionKey);

    Optional<PermissionsEntity> findFirstById(Long id);
}
