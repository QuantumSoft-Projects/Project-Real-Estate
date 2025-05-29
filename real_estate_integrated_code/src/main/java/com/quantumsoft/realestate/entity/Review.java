package com.quantumsoft.realestate.entity;

import com.quantumsoft.realestate.enums.ReviewCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "reviewId")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "propertyId")
    private Property property;
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReviewCategory reviewCategory;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersit(){
        this.createdAt = LocalDateTime.now();
    }

    public Review(){}

    public Review(Long id, Users user, Property property, Integer rating, ReviewCategory reviewCategory, String comment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.property = property;
        this.rating = rating;
        this.reviewCategory = reviewCategory;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public @Min(value = 1) @Max(value = 5) Integer getRating() {
        return rating;
    }

    public void setRating(@Min(value = 1) @Max(value = 5) Integer rating) {
        this.rating = rating;
    }

    public ReviewCategory getReviewCategory() {
        return reviewCategory;
    }

    public void setReviewCategory(ReviewCategory reviewCategory) {
        this.reviewCategory = reviewCategory;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
