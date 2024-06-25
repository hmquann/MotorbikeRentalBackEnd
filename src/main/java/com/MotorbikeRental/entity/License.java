package com.MotorbikeRental.entity;



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
    @Column(name="license_number")
    private String licenseNumber;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate birthOfDate;
    private String licenseImageUrl;
    private boolean status;
}
