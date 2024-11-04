package com.example.user_management_ms.controllers;

import com.example.user_management_ms.dto.security.AccountCredentialsDto;
import com.example.user_management_ms.exceptions.InvalidCredentialsException;
import com.example.user_management_ms.security.jwt.JwtToken;
import com.example.user_management_ms.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<JwtToken> authenticate(@Valid @RequestBody AccountCredentialsDto credentialsDto) throws InvalidCredentialsException {
        JwtToken token = userService.authenticateUser(credentialsDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
