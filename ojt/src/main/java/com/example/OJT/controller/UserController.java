package com.example.OJT.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.OJT.model.User;
import com.example.OJT.service.UserService;
import com.example.OJT.util.JwtUtil;

@RestController
@RequestMapping("/auth")

public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    
    

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        UserDetails foundUser = userService.loadUserByUsername(user.getUsername());

        // Validate password
        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            // Generate a token (not implemented)
            String token = jwtUtil.generateToken(foundUser.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", foundUser.getAuthorities().stream()
            .findFirst().get().getAuthority()); // Send the role back
            return ResponseEntity.ok(response); // You can replace with actual token generation
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
}
