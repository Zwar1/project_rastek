package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "permissions")
public class PermissionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false )
    private String category;

    @Column(name = "sub_category")
    private String subCategory;

    // More granular action/permissions
    @Column(name = "action")
    private String action;

    @Column(name = "description")
    private String description;

    @Column(name = "permission_key", unique = true)
    private String permissionKey; // Generated combination of above fields

    @Column(name = "is_core", nullable = false)
    private boolean isCore = false;

    @PrePersist
    @PreUpdate
    public void generatePermissionKey() {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(category.toUpperCase()).append(":");
        if (subCategory != null) {
            keyBuilder.append(subCategory.toUpperCase()).append(":");
        }
        keyBuilder.append(action.toUpperCase());
        this.permissionKey = keyBuilder.toString();
    }
}
