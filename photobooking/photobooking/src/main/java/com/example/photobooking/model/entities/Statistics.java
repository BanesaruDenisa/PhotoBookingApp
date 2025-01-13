package com.example.photobooking.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "statistics")
@AllArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "Statistics must be associated with a user")
    private User user;

    @Min(value = 0, message = "Total bookings cannot be negative")
    private int totalBookings;

    @Min(value = 0, message = "Estimated earnings cannot be negative")
    private double estimatedEarnings;

    public Statistics() {}


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public double getEstimatedEarnings() {
        return estimatedEarnings;
    }

    public void setEstimatedEarnings(double estimatedEarnings) {
        this.estimatedEarnings = estimatedEarnings;
    }
}
