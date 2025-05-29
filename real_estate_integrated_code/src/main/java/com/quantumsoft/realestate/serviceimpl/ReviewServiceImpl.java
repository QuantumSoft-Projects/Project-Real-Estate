package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Review;
import com.quantumsoft.realestate.exception.ResourceNotFoundException;
import com.quantumsoft.realestate.repository.ReviewRepository;
import com.quantumsoft.realestate.servicei.ReviewServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewServiceI {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public String addReview(Review review) {
        reviewRepository.save(review);
        return "Review record saved successfully...!";
    }

    @Override
    public List<Review> getReviewsByProperty(Long propertyId) {
        return reviewRepository.findAllByProperty_Id(propertyId);
    }

    @Override
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findAllByUser_Id(userId);
    }

    @Override
    public void deleteReview(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if(reviewOptional.isPresent()){
            reviewRepository.delete(reviewOptional.get());
        }else
            throw new ResourceNotFoundException("Review not found in database");
    }

    @Override
    public String updateReview(Long id, Review review) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if(reviewOptional.isPresent()) {
            review.setUpdatedAt(LocalDateTime.now());
            reviewRepository.save(review);
            return "Review updated successfully...!";
        }else
            throw new ResourceNotFoundException("Review not found in database");
    }

    @Override
    public Double averageRatingPerProperty(Long propertyId) {
        List<Review> reviews = reviewRepository.findAllByProperty_Id(propertyId);
        Double totalRating = 0.0;
        int count = 0;
        for (Review review : reviews){
            System.out.println("rating: " + review.getRating());
            totalRating = totalRating + review.getRating();
            count++;
        }
        Double averageRating = totalRating/count;
        DecimalFormat df = new DecimalFormat("0.0"); // to limit the rating value to one decimal place
        return Double.parseDouble(df.format(averageRating));
    }
}
