package com.MotorbikeRental.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "[Model]")

public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="model_name")
    private String modelName;

    @Column(name="cylinder_capacity")
    private String cylinderCapacity;

    @Column(name="fuel_type")
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(name="fuel_consumption")
    private float fuelConsumption;
    @Column(name="model_type")
    @Enumerated(EnumType.STRING)
    private ModelType modelType;
    @ManyToOne
    @JoinColumn(name="brand_id")
    @JsonBackReference
    private Brand brand;
    @OneToMany(mappedBy = "model",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Motorbike>motorbikeSet=new HashSet<>();
}
