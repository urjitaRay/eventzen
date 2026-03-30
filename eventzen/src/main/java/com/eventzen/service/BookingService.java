package com.eventzen.service;

import com.eventzen.model.Booking;
import com.eventzen.model.User;

import java.util.List;

public interface BookingService {

    Booking createBooking(Long eventId, User user);

    List<Booking> getMyBookings(User user);

    void cancelBooking(Long bookingId, User user);

    long getConfirmedBookingCount(Long eventId); // ✅ Get confirmed booking count for event
}