package com.MotorbikeRental.repository;

import com.MotorbikeRental.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoom(String room);

    @Query("SELECT m FROM Message m WHERE m.room LIKE :roomPattern")
    List<Message> getAllMessageByUser(@Param("roomPattern") String roomPattern);

    @Query("SELECT m FROM Message m WHERE m.room LIKE :roomPattern")
    List<Message> getListMessageByUniqueRoom(@Param("roomPattern") String roomPattern);

    @Query("SELECT m FROM Message m WHERE m.room = :roomPattern ORDER BY m.timestamp DESC")
    List<Message> getLastMessageByUniqueRoom(@Param("roomPattern") String roomPattern, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.room LIKE :roomPattern ORDER BY m.timestamp DESC")
    List<Message> getLastMessageAllRoom(@Param("roomPattern") String roomPattern, Pageable pageable);
}
