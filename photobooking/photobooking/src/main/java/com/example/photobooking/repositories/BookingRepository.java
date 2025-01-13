package com.example.photobooking.repositories;

import com.example.photobooking.model.entities.Booking;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {

}
