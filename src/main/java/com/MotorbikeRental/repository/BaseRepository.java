package com.MotorbikeRental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.yaml.snakeyaml.events.Event;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, Event.ID> {

}
