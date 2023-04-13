package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReview();
    List<ReviewDTO> getAllReviewActive();
    ReviewDTO confirmReview(int id);
    ReviewDTO getOneReviewById(int id);
    ReviewDTO createReview(ReviewDTO reviewDTO);
    ReviewDTO updateReview(ReviewDTO reviewDTO);
    void deleteReview(int id);
}
