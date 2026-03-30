package com.eventzen.config;

import com.eventzen.model.Role;
import com.eventzen.model.User;
import com.eventzen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // check if admin already exists
            User existingAdmin = userRepository.findByEmail("admin@mail.com");

            if (existingAdmin == null) {

                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@mail.com");
                admin.setPassword(passwordEncoder.encode("123456")); // BCrypt automatically
                admin.setRole(Role.ADMIN);
                admin.setPhone("9999999999");

                userRepository.save(admin);

                System.out.println("Admin user created");
            } else {
                System.out.println("Admin already exists");
            }
        };
    }
}