package com.example.userCrud.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jabatan")
public class JabatanEntity {

    @Id
    @Column(name = "kode_jabatan", unique = true)
    private String kodeJabatan;

    private String namaJabatan;

    private Long sequence;

    private boolean isAtasan = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_departement", referencedColumnName = "id_departement", nullable = false)
    private DepartementEntity departementEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_division", referencedColumnName = "id_division", nullable = true)
    private DivisionEntity divisionEntity;

}
