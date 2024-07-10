package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Role;
import com.MotorbikeRental.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN u.roles r WHERE (u.email LIKE %:searchTerm% OR u.phone LIKE %:searchTerm%) AND (r.name = 'USER' OR r.name = 'LESSOR')")
    Page<User> findByEmailOrPhone(String searchTerm, Pageable pageable);

    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.email=:email")
    Optional<User> findByEmail2(String email);
    Optional<User> findByToken(String token);

    Optional<User> findByPhone(String phone);

    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name IN ('USER', 'LESSOR')")
    Page<User> findAllUsersWithRoles(Pageable pageable);

    User getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void changePassword(@Param("email") String email, String password);

    @Query("SELECT u.isActive FROM User u WHERE u.id = :id")
    boolean findIsActiveById(@Param("id") Long id);


    @Query("SELECT u FROM User u WHERE u.email = :emailOrPhone OR u.phone = :emailOrPhone")
    Optional<User> findByEmailOrPhone(@Param("emailOrPhone") String emailOrPhone);
    @Query("SELECT CONCAT(u.lastName,' ',u.firstName) FROM User u WHERE u.email = :email")
    String getUserNameByEmail(@Param("email") String email);
}
