package com.eventzen.service;

import com.eventzen.model.*;
import com.eventzen.repository.BookingRepository;
import com.eventzen.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Booking createBooking(Long eventId, User user) {

    Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

    Optional<Booking> existing = bookingRepository.findByUserAndEvent(user, event);

    if (existing.isPresent()) {

        Booking booking = existing.get();

        if (booking.getRsvpStatus() == RsvpStatus.CONFIRMED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, 
                    "Event already booked"
            );
        }

        // If previously cancelled → rebook
        booking.setRsvpStatus(RsvpStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    Booking booking = new Booking();
    booking.setUser(user);
    booking.setEvent(event);
    booking.setRsvpStatus(RsvpStatus.CONFIRMED);

    return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getMyBookings(User user) {
        return bookingRepository.findByUser(user);
    }

    @Override
    public void cancelBooking(Long bookingId, User user) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // ❗ Ensure user owns booking
        if (!booking.getUser().getId().equals(user.getId())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your booking");
    }

        booking.setRsvpStatus(RsvpStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public long getConfirmedBookingCount(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return bookingRepository.countByEventAndRsvpStatus(event, RsvpStatus.CONFIRMED);
    }
}