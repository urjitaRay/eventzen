package com.eventzen.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vendors")
@Data
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;

    private String vendorName;

    private String serviceType;

    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;   // ✅ Proper FK mapping
}