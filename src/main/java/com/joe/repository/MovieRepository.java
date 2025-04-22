package com.joe.repository;

import com.joe.entity.MovieEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    MovieEntity findByTitle(@NotBlank(message = "Movie title cannot be blank") String title);
}
