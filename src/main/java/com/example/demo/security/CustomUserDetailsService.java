package com.example.demo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final Map<String, Map<String, Object>> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Map<String, Object> userData = users.get(email);
        if (userData == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        return User.builder()
                .username(email)
                .password((String) userData.get("password"))
                .roles((String) userData.get("role"))
                .build();
    }
    
    public Map<String, Object> registerUser(String fullName, String email, String encodedPassword, String role) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", idGenerator.getAndIncrement());
        userData.put("fullName", fullName);
        userData.put("email", email);
        userData.put("password", encodedPassword);
        userData.put("role", role);
        
        users.put(email, userData);
        
        return userData;
    }
    
    // ✅ This method must be present
    public Map<String, Object> getUserData(String email) {
        return users.get(email);
    }
}
