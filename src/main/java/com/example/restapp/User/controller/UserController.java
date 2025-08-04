package com.example.restapp.User.controller;

import com.example.restapp.User.model.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/users")
public class UserController {

    public List<User> getAllUsers(){
        return List.of();
    }
}
