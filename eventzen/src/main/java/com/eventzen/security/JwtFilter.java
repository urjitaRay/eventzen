package com.eventzen.security;

import com.eventzen.model.User;
import com.eventzen.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Arrays;
import io.jsonwebtoken.JwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                String email = jwtUtil.extractEmail(token);
                System.out.println("JwtFilter: Extracted email from token: " + email);

                if (email != null) {
                    var currentAuth = SecurityContextHolder.getContext().getAuthentication();
                    if (currentAuth == null || !currentAuth.isAuthenticated()) {
                        User user = userRepository.findByEmail(email);
                        System.out.println("JwtFilter: Looking for user with email: " + email);
                        System.out.println("JwtFilter: User found: " + (user != null));
                        
                        if (user != null) {
                            System.out.println("JwtFilter: User role: " + user.getRole());
                            System.out.println("JwtFilter: User ID: " + user.getId());
                            
                            // ✅ VALIDATE TOKEN
                            boolean tokenValid = jwtUtil.validateToken(token, user);
                            System.out.println("JwtFilter: Token validation result: " + tokenValid);
                            
                            if (tokenValid) {
                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(
                                                user,
                                                null,
                                                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                                        );
                                System.out.println("JwtFilter: Setting authentication for user: " + email + " with role: " + user.getRole().name());
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            } else {
                                System.out.println("JwtFilter: Token validation failed for user: " + email);
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("Invalid token");
                                return;
                            }
                        } else {
                            System.out.println("JwtFilter: User not found in database for email: " + email);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("User not found");
                            return;
                        }
                    } else {
                        System.out.println("JwtFilter: User already authenticated, skipping filter");
                    }
                } else {
                    System.out.println("JwtFilter: Could not extract email from token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token format");
                    return;
                }
            } catch (JwtException e) {
                System.out.println("JwtFilter: JWT exception - " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired or invalid");
                return;
            } catch (Exception e) {
                System.out.println("JwtFilter: Error processing JWT - " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication error");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}