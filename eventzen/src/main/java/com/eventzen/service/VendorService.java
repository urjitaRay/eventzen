package com.eventzen.service;

import com.eventzen.model.Vendor;
import com.eventzen.model.User;

import java.util.List;

public interface VendorService {

    Vendor createVendor(Vendor vendor, Long eventId, User user);

    List<Vendor> getAllVendors();

    List<Vendor> getVendorsByEvent(Long eventId);

    void deleteVendor(Long vendorId, User user);
}