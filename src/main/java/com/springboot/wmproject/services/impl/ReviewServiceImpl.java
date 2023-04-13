package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.ReviewDTO;
import com.springboot.wmproject.entities.Review;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.ReviewRepository;
import com.springboot.wmproject.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReviewDTO> getAllReview()throws ResourceNotFoundException {
        return reviewRepository.findAll().stream().map(review -> mapToDTO(review)).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getAllReviewActive()throws ResourceNotFoundException {
        List<Review> listAll = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOList = listAll.stream().map(review -> mapToDTO(review)).collect(Collectors.toList());
        List<ReviewDTO> finalList = new ArrayList<>();
        for (ReviewDTO item : reviewDTOList
        ) {
            if (item.isActive()) {
                finalList.add(item);
            }
        }
        return finalList;
    }

    @Override
    public ReviewDTO confirmReview(int id) throws ResourceNotFoundException{
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(id)));
        review.setActive(true);
        reviewRepository.save(review);
        return mapToDTO(review);
    }

    @Override
    public ReviewDTO getOneReviewById(int id) throws ResourceNotFoundException{
        return mapToDTO(reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(id))));
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) throws ResourceNotFoundException{
        reviewRepository.save(mapToEntity(reviewDTO));
        return reviewDTO;
    }

    @Override
    public ReviewDTO updateReview(ReviewDTO reviewDTO)throws ResourceNotFoundException {
        Review review = reviewRepository.findById(reviewDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(reviewDTO.getId())));
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        review.setDatePost(reviewDTO.getDatePost());
        review.setActive(reviewDTO.isActive());
        return null;
    }

    @Override
    public void deleteReview(int id) throws ResourceNotFoundException{
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review", "id", String.valueOf(id)));
        reviewRepository.delete(review);
    }

    public ReviewDTO mapToDTO(Review review) {
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        return reviewDTO;
    }

    public Review mapToEntity(ReviewDTO reviewDTO) {
        Review review = modelMapper.map(reviewDTO, Review.class);
        return review;
    }
}
