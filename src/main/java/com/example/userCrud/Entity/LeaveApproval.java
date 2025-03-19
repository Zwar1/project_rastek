package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="leave_approval")
public class LeaveApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_approval")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leaveRequest")
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "Approver", referencedColumnName = "nik")
    private EmployeeEntity approver;

    private String status = "Pending";

    private String comment = "Menunggu Response";

}
