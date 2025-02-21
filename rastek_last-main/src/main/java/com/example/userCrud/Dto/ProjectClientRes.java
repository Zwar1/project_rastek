package com.example.userCrud.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectClientRes {
    private Long id;
    private String name;
    private String clientCountry;
    private String clientAddress;
    private byte[] profilePicture;
    private String profilePictureType;
}
