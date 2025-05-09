package com.joe.auth.controller;

import com.joe.auth.dto.AuthResponse;
import com.joe.auth.dto.LoginRequest;
import com.joe.auth.dto.RefreshTokenRequest;
import com.joe.auth.dto.RegisterRequest;
import com.joe.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> registerUserHandler(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponse> refreshTokenHandler(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
