package com.joe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "movie_tbl")
@Schema(description = "movie_tbl")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @NotBlank(message = "Movie title cannot be blank")
    @Column(nullable = false, length = 200)
    @Schema(description = "Movie Title")
    private String title;

    @NotBlank(message = "Movie director cannot be blank")
    @Column(nullable = false)
    @Schema(description = "Director's full name", example = "John Doe")
    private String director;

    @NotBlank(message = "Movie Release Year cannot be blank")
    @Column(nullable = false)
    @Schema(description = "Movie Release Year", example = "19.02.2025")
    private String releaseYear;

    @NotBlank(message = "Movie Studio cannot be blank")
    @Column(nullable = false)
    @Schema(description = "Studio Name", example = "Disney World Entertainment")
    private String studio;

    @NotBlank(message = "Movie Poster cannot be blank")
    @Column(nullable = false)
    @Schema(description = "Poster Name", example = "Alone 2")
    private String poster;

    @NotBlank(message = "Movie PosterURL cannot be blank")
    @Schema(description = "Poster's URL location")
    private String posterURL;

    @ElementCollection
    @CollectionTable(name = "movie_cast_tbl")
    @Schema(description = "List of Movie's full cast")
    private Set<String> movieCast;

}
