package com.quantumsoft.realestate.serviceimpl;

import com.quantumsoft.realestate.entity.Booking;
import com.quantumsoft.realestate.entity.Property;
import com.quantumsoft.realestate.entity.ScheduleVisit;
import com.quantumsoft.realestate.entity.Users;
import com.quantumsoft.realestate.enums.BookingStatus;
import com.quantumsoft.realestate.enums.PaymentStatus;
import com.quantumsoft.realestate.enums.Role;
import com.quantumsoft.realestate.enums.VisitingMode;
import com.quantumsoft.realestate.exception.ResourceNotFoundException;
import com.quantumsoft.realestate.repository.BookingRepository;
import com.quantumsoft.realestate.repository.PropertyRepository;
import com.quantumsoft.realestate.repository.ScheduleVisitRepository;
import com.quantumsoft.realestate.repository.UserRepository;
import com.quantumsoft.realestate.servicei.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public Booking createBooking(Booking booking, Long propertyId, Long userId) {
//        Property property = propertyRepository.findById(propertyId)
//                .orElseThrow(() -> new RuntimeException("Property not found"));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (bookingRepository.existsByPropertyAndScheduleDate(property, booking.getScheduleDate())) {
//            throw new RuntimeException("Property already booked for this time slot.");
//        }
//
//        booking.setProperty(property);
//        booking.setBuyer(user);
//        booking.setBookingDate(LocalDate.now());
//        booking.setStatus(BookingStatus.PENDING);
//        booking.setPaymentStatus(PENDING);
//
//        return bookingRepository.save(booking);
//    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found."));
    }

    @Override
    public List<Booking> getBookingsByUser(Long buyerId) {
        return bookingRepository.findByBuyer_Id(buyerId);
    }

    @Override
    public void cancelBooking(Long id) {
        Booking booking = getBooking(id);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public String bookProperty(Long propertyId, String buyerEmail, Booking bookingRequest) {

        Users buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        if (buyer.getRole() != Role.BUYER) {
            throw new RuntimeException("Only buyer can book properties");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

//        if (bookingRepository.existsByProperty(property)) {
//            throw new RuntimeException("Property is already booked");
//        }
        if (bookingRequest.getScheduleDate() == null) {
            throw new RuntimeException("Schedule date must be provided.");
        }


        if (bookingRepository.existsByPropertyAndScheduleDate(property, bookingRequest.getScheduleDate())) {
            throw new RuntimeException("Property already booked for this time slot.");
        }

        Booking booking = new Booking();
        booking.setBuyer(buyer);
        booking.setProperty(property);
        //booking.setBookingDate(LocalDate.now());
        booking.setScheduleDate(bookingRequest.getScheduleDate());
        booking.setScheduleTime(bookingRequest.getScheduleTime());
        if(bookingRequest.getVisitingMode().name().equals("PHYSICAL")) {
            booking.setVisitingMode(VisitingMode.PHYSICAL);
        }else{
            booking.setVisitingMode(VisitingMode.VIRTUAL);
        }
        booking.setStatus(bookingRequest.getStatus() != null ? bookingRequest.getStatus() : BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING); // Or however you're handling payment
        System.out.println(booking);
        bookingRepository.save(booking);

        return "Property booked successfully!";
    }

}
