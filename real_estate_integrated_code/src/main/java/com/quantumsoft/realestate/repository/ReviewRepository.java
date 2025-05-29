package com.quantumsoft.realestate.repository;

import com.quantumsoft.realestate.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    public List<Review> findAllByProperty_Id(Long propertyId);
    public List<Review> findAllByUser_Id(Long userId);
}
