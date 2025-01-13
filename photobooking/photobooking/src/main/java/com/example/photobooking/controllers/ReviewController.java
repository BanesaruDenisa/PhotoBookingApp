package com.example.photobooking.controllers;

import com.example.photobooking.model.entities.DTO.ReviewDTO;
import com.example.photobooking.model.entities.Review;
import com.example.photobooking.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Review Controller", description = "Endpoints pentru gestionarea recenziilor.")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    @Operation(
            summary = "Get all reviews",
            description = "Fetch all reviews from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully")
            }
    )
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a review by ID",
            description = "Fetch a specific review by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    public ReviewDTO getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    @Operation(
            summary = "Create a new review",
            description = "Add a new review to the database.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Review created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ReviewDTO createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a review",
            description = "Update the content of an existing review by ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ReviewDTO updateReview(@PathVariable Long id, @RequestBody @Valid Review updatedReview) {
        return reviewService.updateReview(id, updatedReview);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a review",
            description = "Remove a review from the database by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
