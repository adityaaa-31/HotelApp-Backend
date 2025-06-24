package com.example.restapp.controller;

import com.example.restapp.config.jwt.JwtUtil;
import com.example.restapp.dto.request.LoginRequestDTO;
import com.example.restapp.dto.response.LoginResponseDTO;
import com.example.restapp.dto.request.SignUpRequestDTO;
import com.example.restapp.model.User;
import com.example.restapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);

            logger.info("Login successful for {}", request.getEmail());
            return ResponseEntity.ok(new LoginResponseDTO(jwt));

        } catch (BadCredentialsException e) {

            logger.warn("Invalid credentials for {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }
}
