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
    private Enum<FuelType> fuelType;

    @Column(name="fuel_consumption")
    private float fuelConsumption;
    @Column(name="model_type")
    private Enum<ModelType> modelType;
    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand brand;
    @OneToMany(mappedBy = "model",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Motorbike>motorbikeSet=new HashSet<>();
}
