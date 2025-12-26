package com.example.demo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final Map<String, Map<String, Object>> users = new HashMap<>();
    private long userIdCounter = 1;
    
    public Map<String, Object> registerUser(String name, String email, String encodedPassword, String role) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userIdCounter++);
        userData.put("name", name);
        userData.put("email", email);
        userData.put("password", encodedPassword);
        userData.put("role", role);
        
        users.put(email, userData);
        return userData;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Map<String, Object> userData = users.get(email);
        if (userData == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        
        return User.builder()
            .username(email)
            .password((String) userData.get("password"))
            .roles((String) userData.get("role"))
            .build();
    }
}
