package com.eventzen.controller;

import com.eventzen.model.Expense;
import com.eventzen.model.User;
import com.eventzen.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    //  CREATE
    @PostMapping("/{eventId}/{vendorId}")
    public Expense createExpense(@RequestBody Expense expense,
                                 @PathVariable Long eventId,
                                 @PathVariable Long vendorId) {
        return expenseService.createExpense(expense, eventId, vendorId, getCurrentUser());
    }

    //  GET ALL
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    //  GET BY EVENT
    @GetMapping("/event/{eventId}")
    public List<Expense> getByEvent(@PathVariable Long eventId) {
        return expenseService.getExpensesByEvent(eventId);
    }

    //  GET BY VENDOR
    @GetMapping("/vendor/{vendorId}")
    public List<Expense> getByVendor(@PathVariable Long vendorId) {
        return expenseService.getExpensesByVendor(vendorId);
    }

    //  DELETE
    @DeleteMapping("/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId, getCurrentUser());
        return "Expense deleted";
    }
}