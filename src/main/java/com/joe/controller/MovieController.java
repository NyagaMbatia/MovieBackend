package com.joe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.dto.MovieDto;
import com.joe.exception.MovieAlreadyExists;
import com.joe.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
@Tag(name = "Movie Controller", description = "Movie management endpoints")
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @PostMapping("v1/add-movie")
    @Operation(summary = "Adds the movie", description = "Uploads the movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved poster"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view poster"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<MovieDto> uploadMovieHandler(
            @RequestPart MultipartFile file,
            @RequestPart String movieDto
            ) throws IOException, MovieAlreadyExists {
        log.info("----------- Successfully called controller endpoint ----------");
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);

    }

    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(movieDtoObj, MovieDto.class);
    }
}
