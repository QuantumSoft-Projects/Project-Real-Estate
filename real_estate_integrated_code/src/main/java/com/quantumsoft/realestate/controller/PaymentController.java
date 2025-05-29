package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.entity.Payment;
import com.quantumsoft.realestate.servicei.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/initiate")
    public ResponseEntity<Payment> initiatePayment(
            @RequestParam Long bookingId,
            @RequestParam Long userId,
            @RequestParam String paymentMethod,
            @RequestParam Double amount
    ) {
        Payment payment = paymentService.initiatePayment(bookingId, userId, paymentMethod, amount);
        return ResponseEntity.ok(payment);
    }

//    @PutMapping("/update-status")
//    public ResponseEntity<Payment> updatePaymentStatus(
//            @RequestParam String referenceId,
//            @RequestParam String status
//    ) {
//        Payment payment = paymentService.updatePaymentStatus(referenceId, status);
//        return ResponseEntity.ok(payment);
//    }
}
