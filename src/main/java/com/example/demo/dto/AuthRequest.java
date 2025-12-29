package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication request")
public class AuthRequest {
    
    @Schema(description = "User name", example = "John Doe")
    private String name;
    
    @Schema(description = "User email", example = "user@example.com", required = true)
    private String email;
    
    @Schema(description = "Password", example = "password123", required = true)
    private String password;
    
    @Schema(description = "User role", example = "USER")
    private String role;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
