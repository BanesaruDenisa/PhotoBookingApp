package com.example.photobooking.repositories;

import com.example.photobooking.model.entities.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT s FROM Statistics s WHERE s.user.id = :userId")
    Optional<Statistics> findByUserId(Long userId);
}
