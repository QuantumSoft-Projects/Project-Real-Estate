package com.quantumsoft.realestate.repository;

import com.quantumsoft.realestate.entity.Booking;
import com.quantumsoft.realestate.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByBuyer_Id(Long buyerId);

    boolean existsByPropertyAndScheduleDate(Property property, LocalDate scheduleDate);

    boolean existsByProperty(Property property);
}
