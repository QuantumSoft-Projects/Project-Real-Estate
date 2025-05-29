package com.quantumsoft.realestate.entity;

import com.quantumsoft.realestate.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

//    @OneToMany
//    @JoinColumn(name="transection_id")
//    private Transaction transaction;

//    @OneToMany(mappedBy = "payment",cascade = CascadeType.ALL)
//    private List<Transaction> transactions=new ArrayList<>();

    private Double amount;

    private String paymentMethod;
    private LocalDateTime paymentDate;

    //@Column(name="payment_referance_id",unique=true)
    @Column(name = "payment_reference_id")
    private String paymentReferenceId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentReferenceId() {
        return paymentReferenceId;
    }

    public void setPaymentReferenceId(String paymentReferenceId) {
       this. paymentReferenceId = paymentReferenceId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}



//    public Payment(Long id, Booking booking, User user, Double amount, String paymentMethod, LocalDateTime paymentDate, String paymentReferenceId, PaymentStatus status) {
//        this.id = id;
//        this.booking = booking;
//        this.user = user;
//        this.amount = amount;
//        this.paymentMethod = paymentMethod;
//        this.paymentDate = paymentDate;
//        PaymentReferenceId = paymentReferenceId;
//        this.status = status;
//    }
//
//    public void setStatus(PaymentStatus status) {
//        this.status = status;
//
//    }

