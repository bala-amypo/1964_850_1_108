package com.example.demo.controller;

import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public AuthController(CustomUserDetailsService customUserDetailsService,
                         JwtTokenProvider jwtTokenProvider,
                         BCryptPasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        String role = request.get("role");
        
        String encodedPassword = passwordEncoder.encode(password);
        return customUserDetailsService.registerUser(name, email, encodedPassword, role);
    }
    
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        
        // In real implementation, you'd verify password here
        Map<String, Object> userData = Map.of("userId", 1L, "role", "ADMIN");
        
        String token = jwtTokenProvider.generateToken(authentication, 1L, "ADMIN");
        
        return Map.of("token", token, "email", email);
    }
    
    public String getUserData(String email) {
        return email;
    }
}
