package com.example.photobooking.services;

import com.example.photobooking.model.entities.DTO.ReviewDTO;
import com.example.photobooking.model.entities.Review;
import com.example.photobooking.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getBooking().getId()

                ))
                .collect(Collectors.toList());
    }


    public ReviewDTO getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getBooking() != null ? review.getBooking().getId() : null
                ))
                .orElse(null);
    }

    public ReviewDTO createReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        return new ReviewDTO(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getBooking() != null ? savedReview.getBooking().getId() : null
        );
    }

    public ReviewDTO updateReview(Long id, Review updatedReview) {
        return reviewRepository.findById(id)
                .map(existingReview -> {
                    existingReview.setRating(updatedReview.getRating());
                    existingReview.setComment(updatedReview.getComment());
                    Review savedReview = reviewRepository.save(existingReview);
                    return new ReviewDTO(
                            savedReview.getId(),
                            savedReview.getRating(),
                            savedReview.getComment(),
                            savedReview.getBooking() != null ? savedReview.getBooking().getId() : null
                    );
                })
                .orElse(null);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

}
