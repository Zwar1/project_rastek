package com.example.userCrud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee_cv")
public class CVEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cv")
    private Long id;

    private String projectName;
    private String projectRole;
    private LocalDate projectStart;
    private LocalDate projectEnd;
    private String projectDescription;

    @ManyToOne
    @JoinColumn(name = "employee_nik", nullable = false)
    private EmployeeEntity employee;
}
