package com.example.restapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapp.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}