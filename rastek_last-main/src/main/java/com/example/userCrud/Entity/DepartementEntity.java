package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "departement")

public class    DepartementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departement")
    private Long id;

    @Column(name = "departement_name")
    private String departement_name;

    @Column(name = "departement_head", nullable = true)
    private String departement_head;

    // Masih bisa memiliki banyak DivisionEntity tanpa relasi di DivisionEntity
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "departement_id", nullable = true) // FK di tabel division
    private Set<DivisionEntity> divisionEntities = new HashSet<>();

    // Relasi dua arah dengan JabatanEntity
    @OneToMany(mappedBy = "departementEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JabatanEntity> jabatanEntities = new HashSet<>();
}
