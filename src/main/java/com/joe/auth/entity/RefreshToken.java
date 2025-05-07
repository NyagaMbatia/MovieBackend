package com.joe.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "movie-user_refresh_token_tbl")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Token Expiration time should be present")
    private Instant expirationTime;

    @Column(length = 500)
    @NotBlank(message = "Refresh token should be provided")
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
