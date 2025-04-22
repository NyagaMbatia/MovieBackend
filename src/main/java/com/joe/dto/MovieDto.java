package com.joe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MovieDto {
    private Long id;

    @NotBlank(message = "Movie title cannot be blank")
    private String title;

    @NotBlank(message = "Movie director cannot be blank")
    private String director;

    @NotBlank(message = "Movie Release Year cannot be blank")
    private String releaseYear;

    @NotBlank(message = "Movie title cannot be blank")
    private String studio;

    @NotBlank(message = "Movie title cannot be blank")
    private String poster;

    @NotBlank(message = "Movie title cannot be blank")
    private String posterURL;

    private Set<String> movieCast;
}
