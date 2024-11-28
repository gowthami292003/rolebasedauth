package com.example.OJT.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.OJT.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
