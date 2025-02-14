package com.example.userCrud.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRes {
    private Long id;
    private String clientName;
    private String clientContact;
    private String clientEmail;
    private String clientCountry;
    private Boolean isActive;
    private Integer version;
    private String created_by;
    private String update_by;
    private Date createdAt;
    private Date updatedAt;
}
