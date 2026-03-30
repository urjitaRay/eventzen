package com.eventzen.repository;

import com.eventzen.model.Expense;
import com.eventzen.model.Event;
import com.eventzen.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByEvent(Event event); // ✅ Expenses per event

    List<Expense> findByVendor(Vendor vendor); // ✅ Expenses per vendor
}