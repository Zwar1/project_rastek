package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String projectName;
    private String projectDescription;
    private LocalDate projectStart;
    private LocalDate projectEnd;
    private String summary;
    private String priority;
    private String status;
    private Integer estimatedHours;
    private Integer totalHours;
    private Integer progress;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToMany
    @JoinTable(
            name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<EmployeeEntity> member;

    @Column(name = "logo", columnDefinition = "bytea", nullable = true)
    private byte[] logo;

    @Column(name = "logo_type", nullable = true)
    private String logoType;
}
