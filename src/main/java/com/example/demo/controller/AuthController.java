package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    
    // ✅ FIXED: Only 3 dependencies, no UserService
    public AuthController(CustomUserDetailsService customUserDetailsService,
                         JwtTokenProvider jwtTokenProvider,
                         PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse> register(@RequestBody Map<String, String> request) {
        String fullName = request.get("fullName");
        String email = request.get("email");
        String password = request.get("password");
        String role = request.getOrDefault("role", "USER");
        
        String encodedPassword = passwordEncoder.encode(password);
        Map<String, Object> user = customUserDetailsService.registerUser(fullName, email, encodedPassword, role);
        
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", user));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            // Load user details
            org.springframework.security.core.userdetails.UserDetails userDetails = 
                    customUserDetailsService.loadUserByUsername(authRequest.getEmail());
            
            // Verify password
            if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword());
            
            // Get user data for token generation
            Map<String, Object> userData = customUserDetailsService.getUserData(authRequest.getEmail());
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(
                    authentication,
                    (Long) userData.get("userId"),
                    (String) userData.get("role")
            );
            
            // Create response
            AuthResponse response = new AuthResponse(
                    token,
                    (Long) userData.get("userId"),
                    authRequest.getEmail(),
                    (String) userData.get("role")
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
