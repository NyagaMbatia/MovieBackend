package com.joe.auth.service;

import com.joe.auth.entity.RefreshToken;
import com.joe.auth.entity.UserEntity;
import com.joe.auth.repository.RefreshTokenRepository;
import com.joe.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.joe.auth.utils.AuthConstants.JWT_REFRESH_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    public final UserRepository userRepository;
    public final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String email) {
        // 1. Check if the user exists
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user registered with email: " + email));

        // 2. Get the existing refresh token
        RefreshToken refreshToken = user.getRefreshToken();

        // 3. If token is missing, create a new one
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(JWT_REFRESH_EXPIRATION_TIME))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }
    // 4. We check if the refresh token exists and expired else return it
    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refreshToken1 = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Token not found!"));

        if (refreshToken1.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken1);
            throw new RuntimeException("Token is expired!");
        }

        return refreshToken1;
    }

}
