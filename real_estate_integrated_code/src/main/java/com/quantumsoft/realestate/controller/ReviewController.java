package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.entity.Review;
import com.quantumsoft.realestate.servicei.ReviewServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/review")
@CrossOrigin("*")
public class ReviewController {

    @Autowired
    private ReviewServiceI reviewService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping(value = "/add", consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> addReview(@RequestBody Review review){
       return new ResponseEntity<String>(reviewService.addReview(review), HttpStatus.CREATED);
    }

    @GetMapping(value = "/property/{id}", produces = "application/json")
    public ResponseEntity<List<Review>> getReviewsByProperty(@PathVariable Long id){
        return new ResponseEntity<List<Review>>(reviewService.getReviewsByProperty(id), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable Long id){
        return new ResponseEntity<List<Review>>(reviewService.getReviewsByUser(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    @PutMapping(value = "/update/{id}", consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> updateReview(@PathVariable Long id, @RequestBody Review review){
        return new ResponseEntity<String>(reviewService.updateReview(id, review), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return new ResponseEntity<String>("Review deleted successfully...!", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/rating/{id}", produces = "application/json")
    public ResponseEntity<String> averageRatingPerProperty(@PathVariable Long id){
        return new ResponseEntity<String>("Average Rating per Property: " + reviewService.averageRatingPerProperty(id), HttpStatus.OK);
    }
}
