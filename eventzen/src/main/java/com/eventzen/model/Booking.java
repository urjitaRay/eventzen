package com.eventzen.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;   // ✅ FK to users

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event; // ✅ FK to events

    @Enumerated(EnumType.STRING)
    private RsvpStatus rsvpStatus;

    private LocalDate bookingDate;
    @PrePersist
    public void setBookingDate() {
        this.bookingDate = LocalDate.now();
    }
}