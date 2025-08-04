package com.example.restapp.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapp.User.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
}