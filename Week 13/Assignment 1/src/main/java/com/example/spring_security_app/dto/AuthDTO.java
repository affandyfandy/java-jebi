package com.example.spring_security_app.dto;

public class AuthDTO {

    public record LoginRequest(String username, String password) {
    }

    public record Response(String message, String token) {
    }
}
