package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.entity.Payment;
import org.springframework.stereotype.Service;

public interface PaymentService {

    Payment initiatePayment(Long bookingId, Long userId, String paymentMethod, Double amount);

    Payment updatePaymentStatus(String paymentReferenceId, String status);
}

