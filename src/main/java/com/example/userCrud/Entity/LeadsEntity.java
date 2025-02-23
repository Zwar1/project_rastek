package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "leads_entity")
public class LeadsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leads_id")
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_number")
    private String clientNumber;

    @Column(name = "is_approved")
    private boolean isApproved =false;

    @Column(name = "is_rejected")
    private boolean isRejected =false;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "status")
    private String status;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "update_by")
    private String update_by;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
