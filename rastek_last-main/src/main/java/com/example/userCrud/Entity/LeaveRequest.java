package com.example.userCrud.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leave")
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String alasan;

    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "employee_nik", referencedColumnName = "NIK")
    private EmployeeEntity employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_jatahCuti", referencedColumnName = "id_jatahCuti")
    private EmployeeAnnual leaveAnnual;

    @ManyToOne
    @JoinColumn(name = "company_leave_id")
    private CompanyLeave jenisCuti;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_evidence", nullable = true)
    private List<LeaveEvidence> evidences = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_approvers")
    private List<LeaveApproval> approvers = new ArrayList<>();

    @Transient
    private Integer jumlahCuti;
}
