package com.joe.auth.repository;

import com.joe.auth.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.password = ?2 where u.email = ?1 ")
    void updateUserPassword(String email, String encodePassword);
}
