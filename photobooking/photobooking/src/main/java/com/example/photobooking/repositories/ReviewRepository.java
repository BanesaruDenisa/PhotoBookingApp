package com.example.photobooking.repositories;

import com.example.photobooking.model.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
