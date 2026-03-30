package com.eventzen.controller;

import com.eventzen.model.Booking;
import com.eventzen.model.User;
import com.eventzen.repository.BookingRepository;
import com.eventzen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() {

        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        List<User> customers = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole().name().equals("CUSTOMER"))
                .toList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (User user : customers) {

            List<Booking> bookings = bookingRepository.findByUser(user);

            Map<String, Object> userData = new HashMap<>();

            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("phone", user.getPhone());

            List<Map<String, Object>> bookingList = new ArrayList<>();

            for (Booking b : bookings) {
                Map<String, Object> bookingData = new HashMap<>();
                bookingData.put("bookingId", b.getBookingId());
                bookingData.put("eventName", b.getEvent().getEventName());
                bookingData.put("eventDate", b.getEvent().getEventDate());

                bookingList.add(bookingData);
            }

            userData.put("bookings", bookingList);

            result.add(userData);
        }

        return ResponseEntity.ok(result);
    }
}