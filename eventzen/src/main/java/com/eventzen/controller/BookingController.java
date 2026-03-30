package com.eventzen.controller;

import com.eventzen.model.Booking;
import com.eventzen.model.User;
import com.eventzen.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication type");
        }
        return (User) principal;
    }

    //  CREATE BOOKING
    @PostMapping("/{eventId}")
    public Booking createBooking(@PathVariable Long eventId) {
        System.out.println("BookingController: createBooking called for eventId: " + eventId);
        User currentUser = getCurrentUser();
        System.out.println("BookingController: currentUser = " + (currentUser != null ? currentUser.getEmail() : "null"));
        return bookingService.createBooking(eventId, currentUser);
    }

    //  MY BOOKINGS
    @GetMapping("/my")
    public List<Booking> myBookings() {
        return bookingService.getMyBookings(getCurrentUser());
    }

    //  CANCEL BOOKING
    @DeleteMapping("/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId, getCurrentUser());
        return "Booking cancelled";
    }

    //  GET CONFIRMED BOOKING COUNT FOR EVENT (ADMIN ONLY)
    @GetMapping("/count/{eventId}")
    public long getConfirmedBookingCount(@PathVariable Long eventId) {
        User currentUser = getCurrentUser();
        System.out.println("Current user: " + currentUser.getEmail() + ", Role: " + currentUser.getRole());

        // Only allow admins to see booking counts
        if (!currentUser.getRole().name().equals("ADMIN")) {
            System.out.println("Access denied - user is not admin");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can view booking counts");
        }

        System.out.println("Access granted - fetching booking count for event " + eventId);
        return bookingService.getConfirmedBookingCount(eventId);
    }
}