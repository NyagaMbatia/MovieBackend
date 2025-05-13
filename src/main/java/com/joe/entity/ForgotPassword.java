package com.joe.entity;

import com.joe.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "movie_user_forgot-pwd_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpid;

    @Column(nullable = false)
    private Date expirationTime;

    @Column(nullable = false)
    private Integer otp;

    @OneToOne
    private UserEntity user;

}
