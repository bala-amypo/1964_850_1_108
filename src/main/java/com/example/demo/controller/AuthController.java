package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ApiResponse;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthController(CustomUserDetailsService customUserDetailsService,
                         BCryptPasswordEncoder passwordEncoder,
                         JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Map<String, Object> user = customUserDetailsService.registerUser(
            request.getName() != null ? request.getName() : "User",
            request.getEmail(),
            encodedPassword,
            request.getRole() != null ? request.getRole() : "USER"
        );
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            );
            
            // Verify user exists
            customUserDetailsService.loadUserByUsername(request.getEmail());
            
            // Generate token
            String token = jwtTokenProvider.generateToken(authentication, 1L, "USER");
            
            return ResponseEntity.ok(new AuthResponse(token, request.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid credentials"));
        }
    }
}
