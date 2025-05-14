package com.example.userCrud.Entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false )
    private String name;

    @Column(name = "status", nullable = false)
    private String status;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "updated_by")
    private String update_by;

    @Column(name = "created_by")
    private String created_by;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermissionEntity> permissions = new HashSet<>();

    // Helper methods to manage permissions
    public void addPermission(PermissionsEntity permission) {
        RolePermissionEntity rolePermission = new RolePermissionEntity();
        rolePermission.setRole(this);
        rolePermission.setPermission(permission);
        rolePermission.setAllowed(true);
        this.permissions.add(rolePermission);
    }

    public void removePermission(PermissionsEntity permission) {
        this.permissions.removeIf(rp ->
                rp.getPermission().getId().equals(permission.getId()));
    }

    public boolean hasPermission(String permissionKey) {
        return this.permissions.stream()
                .anyMatch(rp -> rp.isAllowed() &&
                        rp.getPermission().getPermissionKey().equals(permissionKey));
    }
}
