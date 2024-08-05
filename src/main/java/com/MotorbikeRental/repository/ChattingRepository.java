package com.MotorbikeRental.repository;

import com.MotorbikeRental.dto.ChattingDto;
import com.MotorbikeRental.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {

    Chatting findChattingByEmailUser1AndEmailUser2(String emailUser1, String emailUser2);

    @Query("SELECT c FROM Chatting c WHERE c.emailUser1 = :email OR c.emailUser2 = :email")
    List<Chatting> findByUserEmail(@Param("email") String email);
}
