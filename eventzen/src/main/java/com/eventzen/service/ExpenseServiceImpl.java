package com.eventzen.service;

import com.eventzen.model.*;
import com.eventzen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public Expense createExpense(Expense expense, Long eventId, Long vendorId, User user) {

        // 🔐 Only ADMIN can add expenses
        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can add expenses");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // ❗ Ensure vendor belongs to same event
        if (!vendor.getEvent().getEventId().equals(event.getEventId())) {
            throw new RuntimeException("Vendor does not belong to this event");
        }

        expense.setEvent(event);
        expense.setVendor(vendor);

        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> getExpensesByEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return expenseRepository.findByEvent(event);
    }

    @Override
    public List<Expense> getExpensesByVendor(Long vendorId) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        return expenseRepository.findByVendor(vendor);
    }

    @Override
    public void deleteExpense(Long expenseId, User user) {

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can delete expenses");
        }

        if (!expenseRepository.existsById(expenseId)) {
            throw new RuntimeException("Expense not found");
        }

        expenseRepository.deleteById(expenseId);
    }
}