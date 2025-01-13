package com.example.photobooking.repositories;

import com.example.photobooking.model.entities.Packages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Packages, Long> {
}
