package com.example.photobooking.model.entities.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;


public class ReviewDTO {
    private Long id;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating cannot be less than 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;


    public ReviewDTO(){

    }

    public ReviewDTO(Long id, Integer rating, String comment, Long bookingId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.bookingId = bookingId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String content) {
        this.comment = content;
    }
}

