package com.example.restapp.controller;

import com.example.restapp.dto.SignUpRequestDTO;
import com.example.restapp.model.User;
import com.example.restapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.restapp.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> createUser(@Valid @RequestBody SignUpRequestDTO signUpRequest) {

        try {
            User user = User.builder()
                    .email(signUpRequest.getEmail())
                    .name(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .build();

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
