package com.eventzen.service;

import com.eventzen.model.*;
import com.eventzen.repository.EventRepository;
import com.eventzen.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Vendor createVendor(Vendor vendor, Long eventId, User user) {

        // 🔐 Only ADMIN can add vendors
        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can add vendors");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        vendor.setEvent(event);

        return vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public List<Vendor> getVendorsByEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return vendorRepository.findByEvent(event);
    }

    @Override
    public void deleteVendor(Long vendorId, User user) {

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can delete vendors");
        }

        if (!vendorRepository.existsById(vendorId)) {
            throw new RuntimeException("Vendor not found");
        }

        vendorRepository.deleteById(vendorId);
    }
}