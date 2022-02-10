package com.example.controller;

import com.example.dto.AuthenticationResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.security.JwtTokenService;
import com.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    @PostMapping(value = "/perform_login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.getToken())
                .body(response);
    }

    @PostMapping("/perform_signup")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/account-verification")
    public ResponseEntity<Void> authenticateUser(@RequestParam String token) {
        jwtTokenService.enableUser(token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}