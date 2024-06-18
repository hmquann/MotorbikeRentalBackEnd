package com.MotorbikeRental.entity;


import com.fasterxml.jackson.annotation.*;
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
@Table(name = "[ModelService]")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="model_name")
    private String modelName;

    @Column(name="cylinder_capacity")
    private int cylinderCapacity;

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
