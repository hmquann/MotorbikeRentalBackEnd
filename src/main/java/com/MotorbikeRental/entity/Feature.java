package com.MotorbikeRental.entity;
import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table (name = "Feature")

public class Feature {
    @Id
    private Long id;

    @Column(name = "feature_name")
    private  String featureName;

    private  String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "Motorbike_Feature",
            joinColumns = @JoinColumn(name = "motorbike_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Motorbike> motorbikeSet = new HashSet<>();
}
