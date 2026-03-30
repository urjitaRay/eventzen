package com.eventzen.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String eventName;

    private String eventType;

    private LocalDate eventDate;

    private LocalTime eventTime;

    private String venue;

    private String description;

    private Double budget;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;   // ✅ Proper FK mapping
}