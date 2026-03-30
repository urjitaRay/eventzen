package com.eventzen.repository;

import com.eventzen.model.Booking;
import com.eventzen.model.User;
import com.eventzen.model.Event;
import com.eventzen.model.RsvpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);          // ✅ My bookings
    List<Booking> findByEvent(Event event);       // ✅ Event bookings
    Optional<Booking> findByUserAndEvent(User user, Event event);
    boolean existsByUserAndEvent(User user, Event event); // ✅ prevent duplicate booking

    long countByEventAndRsvpStatus(Event event, RsvpStatus status); // ✅ Count confirmed bookings
}