package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.entity.Booking;
import com.quantumsoft.realestate.entity.ScheduleVisit;

import java.util.List;

public interface BookingService {
//    Booking createBooking(Booking booking, Long propertyId, Long userId);
    Booking getBooking(Long id);
    List<Booking> getBookingsByUser(Long buyerId);
    void cancelBooking(Long id);
    void deleteBooking(Long id);

    String bookProperty(Long propertyId, String buyerEmail, Booking bookingRequest);
}
