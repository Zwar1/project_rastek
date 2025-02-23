package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "riwayat_jabatan")
public class RiwayatJabatanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riwayat")
    private Long id;

    private String statusKontrak;
    private String tmt_mulai;
    private String tmt_akhir;
    private String kontrakKedua;
    private BigDecimal salary;

    private boolean isCurrent=true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kode_jabatan")
    private JabatanEntity kode_jabatan;

//    @ManyToOne
//    @JoinColumn(name = "employee_nik")
//    private EmployeeEntity employee;

}
