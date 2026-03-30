package com.eventzen.controller;

import com.eventzen.dto.LoginRequest;
import com.eventzen.model.Role;
import com.eventzen.model.User;
import com.eventzen.repository.UserRepository;
import com.eventzen.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    // REGISTER
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // Create a new User object to ensure clean state
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhone(user.getPhone());
        
        // Always set role to CUSTOMER for new registrations
        // Admin roles must be set manually in the database
        newUser.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(newUser);
        System.out.println("AuthController: Final role in saved user: " + savedUser.getRole());
        
        // Return a simple success message
        return ResponseEntity.ok("Registration successful. Your account has been created with CUSTOMER role.");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null ||
                !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(token);
    }
}