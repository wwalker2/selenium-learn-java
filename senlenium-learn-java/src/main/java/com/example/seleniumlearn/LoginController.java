package com.example.seleniumlearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final AuthenticationService authService;

    @Autowired
    public LoginController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        boolean ok = authService.authenticate(username, password);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", ok);
        if (ok) {
            resp.put("message", "Welcome, " + username + "!");
        } else {
            resp.put("message", "Invalid credentials");
        }
        return resp;
    }
}
