package com.example.seleniumlearn;

import org.springframework.stereotype.Service;

// Service that performs authentication checks. For demo purposes this uses an in-memory check.
@Service
public class AuthenticationService {

    /**
     * Simple demo authentication. In real apps replace with a user store and password hashing.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        return "testuser".equals(username) && "password123".equals(password);
    }
}
