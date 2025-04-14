package com.example.userCrud.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_task")
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projectTask")
    private Long idProjectTask;

    @NotBlank(message = "Title is required")
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "progress")
    private Integer progress = 0;

    @Column(name = "onHold")
    private boolean onHold = false;

    // Task member based on employee that part of the project
    @ManyToMany
    @JoinTable(
            name = "projectTask_member",
            joinColumns = @JoinColumn(name = "id_projectTask"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<EmployeeEntity> taskMember;

    // Task project based on project that contains the task
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    public Long getProjectId() {
        return project.getId();
    }
}
