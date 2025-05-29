package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Booking;
import com.quantumsoft.realestate.entity.Payment;
import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.enums.PaymentStatus;
import com.quantumsoft.realestate.repository.BookingRepository;
import com.quantumsoft.realestate.repository.PaymentRepository;
import com.quantumsoft.realestate.repository.UserRepository;
import com.quantumsoft.realestate.servicei.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl  implements PaymentService {
    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Payment initiatePayment(Long bookingId, Long userId, String paymentMethod, Double amount) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        Payment payment = Payment.builder()
//                .paymentMethod(paymentMethod)
//                .paymentStatus("PENDING")
//                .paymentDate(LocalDateTime.now())
//                .amount(amount)
//                .paymentReferenceId(UUID.randomUUID().toString())
//                .booking(booking)
//                .user(user)
//                .build();

        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(amount);
        payment.setPaymentReferenceId(UUID.randomUUID().toString());
        payment.setBooking(booking);
        payment.setUser(user);
        Map<String, String> notification = new HashMap<>();
        notification.put("message", "Hi " + user.getName() + "\n \n" + "Your payment is " + payment.getPaymentStatus().name());
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        return (Payment) paymentRepository.save(payment);
    }



    @Override
    public Payment updatePaymentStatus(String paymentReferenceId, String status) {
        Payment payment = paymentRepository.findByPaymentReferenceId(paymentReferenceId)

                .orElseThrow(() -> new RuntimeException("Payment not found"));

        try{
      payment.setPaymentStatus(PaymentStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Payament Status:"+status);
        }

        return paymentRepository.save(payment);

    }
}
