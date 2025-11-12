package com.example.seleniumlearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CreateAccountController {

    private final AuthenticationService authService;

    @Autowired
    public CreateAccountController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/create-account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> createAccount(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Map<String, Object> resp = new HashMap<>();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            resp.put("success", false);
            resp.put("message", "Username and password are required");
            return resp;
        }

        boolean created = authService.createAccount(username.trim(), password);
        resp.put("success", created);
        if (created) {
            resp.put("message", "Account created for " + username);
        } else {
            resp.put("message", "Account already exists");
        }
        return resp;
    }
}
