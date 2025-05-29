package com.quantumsoft.realestate.controller;

import com.quantumsoft.realestate.entity.Booking;
import com.quantumsoft.realestate.entity.ScheduleVisit;
import com.quantumsoft.realestate.servicei.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // api to schedule the property visit

    @PostMapping("/property/{propertyId}")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<String> bookProperty(@PathVariable Long propertyId, @RequestBody Booking bookingRequest) {
        // Get current authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

         // Call service method
        String result = bookingService.bookProperty(propertyId, email, bookingRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/user/{buyerId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long buyerId) {
        List<Booking> bookings = bookingService.getBookingsByUser(buyerId);
        return ResponseEntity.ok(bookings);
    }

    @PreAuthorize("hasRole('BUYER')")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted.");
    }
}
