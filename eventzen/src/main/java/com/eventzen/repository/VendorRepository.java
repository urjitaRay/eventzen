package com.eventzen.repository;

import com.eventzen.model.Vendor;
import com.eventzen.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findByEvent(Event event); // ✅ Vendors for a specific event
}