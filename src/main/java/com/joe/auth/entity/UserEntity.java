package com.joe.auth;

import com.joe.auth.entity.RefreshToken;
import com.joe.auth.entity.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "movie_user_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    @NotBlank(message = "Name cannot be blank")
    private String userName;

    @Size(message = "Password cannot be less than 7 or greater than 15", min = 7, max = 15)
    @Pattern(message = "Password should be alphanumeric", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$ ")
    @NotBlank(message = "Name cannot be blank")
    private String password;

    @Column(unique = true)
    @NotBlank(message = "Name cannot be blank")
    @Email(message = "Email format not correct, please input a proper email")
    private String email;

    @OneToOne(mappedBy = "user")
    private RefreshToken token;

    private boolean isEnabled = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isAccountNonLocked = true;
    private Boolean isAccountNonExpired = true;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
