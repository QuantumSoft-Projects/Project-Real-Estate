package com.quantumsoft.realestate.entity;

import com.quantumsoft.realestate.enums.BookingStatus;
import com.quantumsoft.realestate.enums.PaymentStatus;
import com.quantumsoft.realestate.enums.VisitingMode;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings", uniqueConstraints = {
                                                @UniqueConstraint(columnNames = {"property_id",
                                                        "scheduleDate"})
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users buyer;
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private LocalDate bookingDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    @Enumerated(EnumType.STRING)
    private VisitingMode visitingMode;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Users getBuyer() {
        return buyer;
    }

    public void setBuyer(Users buyer) {
        this.buyer = buyer;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public LocalTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public VisitingMode getVisitingMode() {
        return visitingMode;
    }

    public void setVisitingMode(VisitingMode visitingMode) {
        this.visitingMode = visitingMode;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
