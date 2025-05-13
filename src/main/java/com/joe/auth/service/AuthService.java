package com.joe.auth.service;

import com.joe.auth.dto.AuthResponse;
import com.joe.auth.dto.LoginRequest;
import com.joe.auth.dto.RefreshTokenRequest;
import com.joe.auth.dto.RegisterRequest;
import com.joe.auth.entity.RefreshToken;
import com.joe.auth.entity.UserEntity;
import com.joe.auth.entity.UserRole;
import com.joe.auth.repository.UserRepository;
import com.joe.exception.CustomBadCredentialsException;
import com.joe.exception.CustomUsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
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

    public AuthResponse login(LoginRequest loginRequest) {
        log.info("---------------- Authenticating user: {} ---------------------",
                loginRequest.getEmail());

        try {
            // First check if user exists in database
            UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new CustomUsernameNotFoundException(
                            "No user is registered with email: " + loginRequest.getEmail()));

            // Then attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("------------------- User Authenticated successfully: {} --------------------",
                    loginRequest.getEmail());

            var accessToken = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user {}: Bad credentials", loginRequest.getEmail());
            throw new CustomBadCredentialsException("Invalid email or password");
        } catch (DisabledException e) {
            log.error("Authentication failed for user {}: Account disabled", loginRequest.getEmail());
            throw new DisabledException("Your account is disabled");
        } catch (LockedException e) {
            log.error("Authentication failed for user {}: Account locked", loginRequest.getEmail());
            throw new LockedException("Your account is locked");
        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new AuthenticationServiceException("Authentication failed: " + e.getMessage());
        }
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
