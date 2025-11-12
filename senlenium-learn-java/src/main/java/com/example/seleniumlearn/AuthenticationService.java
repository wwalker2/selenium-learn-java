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

    // Very small in-memory user store for demo/create-account purposes.
    // NOTE: This is non-persistent and not secure — intended for tests and learning only.
    private final java.util.Set<String> users = new java.util.concurrent.ConcurrentSkipListSet<>();

    /**
     * Create an account in the in-memory store. Returns false if user already exists.
     */
    public boolean createAccount(String username, String password) {
        // Basic validation: password is ignored for storage in this demo.
        if (username == null || username.trim().isEmpty()) return false;
        // try to add; return true only when added successfully
        return users.add(username);
    }

    /**
     * Helper used by tests to clear the demo store.
     */
    public void clearDemoUsers() {
        users.clear();
    }
}
