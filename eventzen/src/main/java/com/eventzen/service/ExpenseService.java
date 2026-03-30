package com.eventzen.service;

import com.eventzen.model.Expense;
import com.eventzen.model.User;

import java.util.List;

public interface ExpenseService {

    Expense createExpense(Expense expense, Long eventId, Long vendorId, User user);

    List<Expense> getAllExpenses();

    List<Expense> getExpensesByEvent(Long eventId);

    List<Expense> getExpensesByVendor(Long vendorId);

    void deleteExpense(Long expenseId, User user);
}