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
@Table(name="company_leave")
public class CompanyLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_CompanyLeave")
    private Long id;

//    @ManyToMany(mappedBy = "companyLeave")
//    private List<EmployeeAnnual> employeeAnnuals = new ArrayList<>();

    private String jenisCuti;

    private Integer jatahawal = 0;
}
