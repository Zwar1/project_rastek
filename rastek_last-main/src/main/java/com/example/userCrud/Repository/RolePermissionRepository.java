package com.example.userCrud.Repository;

import com.example.userCrud.Entity.RolePermissionEntity;
import com.example.userCrud.Entity.RolePermissionId;
import com.example.userCrud.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {

    // Find by role ID and permission ID (using the composite key)
    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
    Optional<RolePermissionEntity> findByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    // Find all permissions for a given role ID
    @Query("SELECT rp FROM RolePermissionEntity rp WHERE rp.role.id = :roleId")
    List<RolePermissionEntity> findByRoleId(@Param("roleId") Long roleId);

    // Delete by role ID
    void deleteByRoleId(Long roleId);
}