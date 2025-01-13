package com.example.photobooking.model.entities.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;


public class BookingDTO {

    private Long id;
    @NotBlank(message = "Location name is required")
    private String locationName;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Booking date cannot be in the past")
    private LocalDate date;

    @Pattern(regexp = "pending|confirmed|cancelled",
            message = "Status must be either 'pending', 'confirmed', or 'cancelled'")
    private String status;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Package ID is required")
    private Long packageId;

    public BookingDTO(Long id, String locationName, LocalDate date, String status, Long userId, Long packageId) {
        this.id = id;
        this.locationName = locationName;
        this.date = date;
        this.status = status;
        this.userId = userId;
        this.packageId = packageId;
    }

    public BookingDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() { return locationName; }

    public void setLocationName(String locationName) { this.locationName = locationName; }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
}
