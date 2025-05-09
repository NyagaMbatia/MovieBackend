package com.joe.auth.service;

import com.joe.auth.dto.AuthResponse;
import com.joe.auth.dto.LoginRequest;
import com.joe.auth.dto.RefreshTokenRequest;
import com.joe.auth.dto.RegisterRequest;
import com.joe.auth.entity.RefreshToken;
import com.joe.auth.entity.UserEntity;
import com.joe.auth.entity.UserRole;
import com.joe.auth.repository.UserRepository;
import com.joe.exception.CustomUsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        var user = UserEntity.builder()
                .userName(registerRequest.getName())
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .password(encoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();
        UserEntity savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        String existingEmail = loginRequest.getEmail();

        UserEntity user = userRepository.findByEmail(existingEmail)
                .orElseThrow(
                        () ->  new CustomUsernameNotFoundException("No user is registered with email : " + existingEmail)
                );

        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(existingEmail);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        UserEntity user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build();
    }
}
