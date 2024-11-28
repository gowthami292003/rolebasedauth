package com.example.OJT.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.OJT.model.User;
import com.example.OJT.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;


@Service
public class UserService implements UserDetailsService {
     private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    public User registerUser(User user) {
        
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password before saving
        
        return userRepository.save(user); // Save user to database
    }

    // Load user by username (used for authentication)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole() != null ? Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) : Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))) // Default to USER if role is null
                .build();
    }

}
