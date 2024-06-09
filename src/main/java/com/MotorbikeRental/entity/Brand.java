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
@Table(name = "[Brand]")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name="origin")
    private String origin;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Model>modelSet=new HashSet<>();
}
