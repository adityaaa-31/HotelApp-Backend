package com.example.restapp.User.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    public LoginResponseDTO(String token) {
        this.token = token;
    }
}
