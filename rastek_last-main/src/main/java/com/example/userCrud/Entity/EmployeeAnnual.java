package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee_annual")
public class EmployeeAnnual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jatahCuti")
    private Long id;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "employee_annual_leave",
//            joinColumns = @JoinColumn(name = "id_jatahCuti"),
//            inverseJoinColumns = @JoinColumn(name = "id_CompanyLeave")
//    )
//    private List<CompanyLeave> companyLeave = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee", referencedColumnName = "NIK", nullable = true)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_cuti", referencedColumnName = "id_CompanyLeave", nullable = true)
    private CompanyLeave companyLeave;

    private Integer sisaCuti;



//    @ElementCollection
//    @CollectionTable(name = "employee_sisa_cuti", joinColumns = @JoinColumn(name = "id_jatahCuti"))
//    @Column(name = "sisa_cuti")
//    private List<Long> sisaCuti = new ArrayList<>();


}
