package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCVRes {
    // employee's name, NIK, CV list encapsulation
    private String nama;
    private long nik;

    private List<CVRes> cv;
}
