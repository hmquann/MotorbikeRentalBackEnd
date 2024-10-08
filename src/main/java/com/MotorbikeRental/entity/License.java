package com.MotorbikeRental.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "[License]")

public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_id")
    private Long id;
    private String licenseNumber;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate birthOfDate;

    private String licenseImageUrl;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    @Enumerated(EnumType.STRING)
    private LicenseStatus status;




}
