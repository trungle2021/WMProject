package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.BookingDTO;
import com.springboot.wmproject.DTO.ReviewDTO;
import com.springboot.wmproject.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/all"})
    public ResponseEntity<List<ReviewDTO>> GetAllReview() {
        return ResponseEntity.ok(reviewService.getAllReview());
    }

//    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/allActive"})
    public ResponseEntity<List<ReviewDTO>> GetAllReviewActive() {
        return ResponseEntity.ok(reviewService.getAllReviewActive());
    }

//    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = {"/confirm/{id}"})
    public ResponseEntity<String> ConfirmReview(@Valid @PathVariable int id) {
        reviewService.confirmReview(id);
        return ResponseEntity.ok("Review has been confirm");
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = {"/one/{id}"})
    public ResponseEntity<ReviewDTO> getOneById(@Valid @PathVariable int id) {
        return ResponseEntity.ok(reviewService.getOneReviewById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = {"/create"})
    public ResponseEntity<ReviewDTO> creatReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.createReview(reviewDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = {"/update"})
    public ResponseEntity<ReviewDTO> updateReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(reviewDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<String> deleteReview(@Valid @PathVariable int id)
    {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review has been deleted");
    }
}
