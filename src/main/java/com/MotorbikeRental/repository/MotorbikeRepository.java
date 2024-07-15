package com.MotorbikeRental.repository;


import com.MotorbikeRental.entity.Motorbike;
import com.MotorbikeRental.entity.MotorbikeStatus;
import com.MotorbikeRental.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotorbikeRepository extends JpaRepository<Motorbike, Long>{
    @Override
    Optional<Motorbike> findById(Long id);
    @Query("SELECT m from Motorbike m where m.motorbikePlate=:motorbikePlate")
    Motorbike findByMotorbikePlate(String motorbikePlate);

    @Query("SELECT m FROM Motorbike m WHERE m.motorbikePlate LIKE %:searchTerm% AND m.user.id = :userId")
    Page<Motorbike> searchMotorbikePlateByLessor(String searchTerm,Long userId, Pageable pageable);

    @Query("SELECT m FROM Motorbike m WHERE m.motorbikePlate LIKE %:searchTerm% AND m.user.id = :userId AND m.status = :status")
    Page<Motorbike> searchMotorbikePlateAndStatusByLessor(String searchTerm,MotorbikeStatus status,Long userId, Pageable pageable);

    @Query("SELECT m FROM Motorbike m WHERE m.motorbikePlate LIKE %:searchTerm% AND m.status = :status")
    Page<Motorbike> searchMotorbikePlateAndStatus(String searchTerm,MotorbikeStatus status,Pageable pageable);

    @Query("SELECT m FROM Motorbike m WHERE m.motorbikePlate LIKE %:searchTerm%")
    Page<Motorbike> searchAllMotorbikePlate(String searchTerm,Pageable pageable);

    List<Motorbike> findByStatus(MotorbikeStatus status);

    boolean existsByMotorbikePlate (String motorbikePlate);
    @Query("SELECT m from Motorbike m where m.status=:status")
    List<Motorbike>getAllMotorbikeByStatus(MotorbikeStatus status);

    @Query("SELECT DISTINCT m FROM Motorbike m JOIN m.user.roles r WHERE r.name IN :roles AND m.user.id = :userId")
    Page<Motorbike> findAllByOwner(@Param("roles") List<String> roles, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT m FROM Motorbike m WHERE m.status = :status AND m.user.id = :userId")
    Page<Motorbike> findAllByStatusByLessor(@Param("status") MotorbikeStatus status,Long userId, Pageable pageable);

    @Query("SELECT m FROM Motorbike m WHERE m.status = :status ")
    Page<Motorbike> findAllByStatus(@Param("status") MotorbikeStatus status, Pageable pageable);
    @Query("SELECT COUNT(m)from Motorbike  m where m.user.id=:id")
    Long countMotorbikeByUser(Long id);
}
