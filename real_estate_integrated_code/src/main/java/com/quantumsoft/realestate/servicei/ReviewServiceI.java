package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.entity.Review;

import java.util.List;

public interface ReviewServiceI {

    public String addReview(Review review); // by user

    public List<Review> getReviewsByProperty(Long propertyId); // by property

    public List<Review> getReviewsByUser(Long userId);

    public void deleteReview(Long id); // by user and admin

    public String updateReview(Long id, Review review); // by user and admin

    public Double averageRatingPerProperty(Long propertyId);
}
