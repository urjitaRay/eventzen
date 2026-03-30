package com.eventzen.controller;

import com.eventzen.model.User;
import com.eventzen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // ✅ GET ALL USERS
    @GetMapping
    public ResponseEntity<?> getAllUsers() {

        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(userRepository.findAll());
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("User not found");
        }

        //  ADD SECURITY CHECK HERE
        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(user.get());
    }

    //  UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

        Optional<User> existingUser = userRepository.findById(id);


        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("User not found");
        }

        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!currentUser.getRole().name().equals("ADMIN") &&
                !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        User user = existingUser.get();




        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());

//  encode password
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));


        user.setPhone(updatedUser.getPhone());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    //  DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        User currentUser = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!currentUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).body("Access denied");
        }

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body("User not found");
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok("User deleted successfully");
    }
}