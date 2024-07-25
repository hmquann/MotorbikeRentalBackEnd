package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
