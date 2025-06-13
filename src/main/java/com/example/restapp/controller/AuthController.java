package com.example.restapp.controller;

import com.example.restapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.restapp.model.User;
import com.example.restapp.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private UserService userService;

    private UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {

        try {
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

            if(existingUser.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("User Already Exists!");
            }

            userService.createUser(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(("User created successfully"));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong!");
        }
    }

}
