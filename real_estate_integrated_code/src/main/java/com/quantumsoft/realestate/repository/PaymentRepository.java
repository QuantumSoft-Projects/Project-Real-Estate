package com.quantumsoft.realestate.repository;

import com.quantumsoft.realestate.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingBookingId(Long bookingId);

    Optional<Payment> findByPaymentReferenceId(String paymentReferenceId);
}
