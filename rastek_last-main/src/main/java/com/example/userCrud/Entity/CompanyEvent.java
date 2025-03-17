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
@Table(name="company_event")
public class CompanyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_companyEvent")
    private Long id;

    private String eventName;

    private Boolean isFree;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_Calendar", nullable = true)
    private List<CompanyCalendar> calendars = new ArrayList<>();
}
