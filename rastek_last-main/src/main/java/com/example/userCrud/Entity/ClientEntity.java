package com.example.userCrud.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_contact")
    private String clientContact;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "client_country")
    private String clientCountry;

    @Column(name = "client_address")
    private String clientAddress;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "pic_number")
    private Integer picNumber;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

    @Column(name = "password")
    private String password;

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

    @Column(name = "profile_picture", columnDefinition = "bytea", nullable = true)
    private byte[] profilePicture;

    @Column(name = "profile_picture_type", nullable = true)
    private String profilePictureType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = true)
    private User user;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ProjectEntity> projects;
}
