package com.MotorbikeRental.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "[License]")

public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="license_id")
    private Long id;
    @Column(name = "user_id")
    @OneToOne(mappedBy = "id")
    private User user;;

}
