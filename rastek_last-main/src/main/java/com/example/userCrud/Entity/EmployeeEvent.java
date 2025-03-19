package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="employee_event")
public class EmployeeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employeeEvent")
    private Long id;

    private String eventName;

    private Boolean isCuti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NIK", referencedColumnName = "NIK")
    private EmployeeEntity employee;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_employeeCalendar", nullable = true)
    private List<EmployeeCalendar> employeeCalendars = new ArrayList<>();
}
