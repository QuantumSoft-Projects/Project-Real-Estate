package com.quantumsoft.realestate.repository;


import com.quantumsoft.realestate.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByDeletedFalse();
     List<Property> findByPriceBetweenAndLocationContainingIgnoreCaseAndTypeContainingIgnoreCaseAndDeletedFalse(
     Double min, Double max, String location, String type);
}

