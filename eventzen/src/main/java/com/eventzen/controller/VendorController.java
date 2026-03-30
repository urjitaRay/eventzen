package com.eventzen.controller;

import com.eventzen.model.User;
import com.eventzen.model.Vendor;
import com.eventzen.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    // ✅ CREATE VENDOR (linked to event)
    @PostMapping("/{eventId}")
    public Vendor createVendor(@RequestBody Vendor vendor,
                               @PathVariable Long eventId) {
        return vendorService.createVendor(vendor, eventId, getCurrentUser());
    }

    // ✅ GET ALL
    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorService.getAllVendors();
    }

    // ✅ GET BY EVENT
    @GetMapping("/event/{eventId}")
    public List<Vendor> getVendorsByEvent(@PathVariable Long eventId) {
        return vendorService.getVendorsByEvent(eventId);
    }

    // ✅ DELETE
    @DeleteMapping("/{vendorId}")
    public String deleteVendor(@PathVariable Long vendorId) {
        vendorService.deleteVendor(vendorId, getCurrentUser());
        return "Vendor deleted";
    }
}