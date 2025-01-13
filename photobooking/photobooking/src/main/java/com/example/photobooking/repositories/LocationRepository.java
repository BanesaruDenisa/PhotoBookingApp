package com.example.photobooking.repositories;

import com.example.photobooking.model.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.name = :name")
    Optional<Location> findByName(String name);

}
