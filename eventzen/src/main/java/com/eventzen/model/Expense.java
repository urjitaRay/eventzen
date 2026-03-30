package com.eventzen.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;   // ✅ FK to events

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor; // ✅ FK to vendors

    private Double amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDate expenseDate;
    @PrePersist
    public void setExpenseDate() {
        this.expenseDate = LocalDate.now();
    }
}