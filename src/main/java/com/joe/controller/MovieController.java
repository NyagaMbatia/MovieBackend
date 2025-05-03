package com.joe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.dto.MovieDto;
import com.joe.exception.MovieAlreadyExists;
import com.joe.exception.MovieDoesNotExist;
import com.joe.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view movie"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<MovieDto> uploadMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException, MovieAlreadyExists {
        log.info("----------- Successfully called controller endpoint ----------");
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);

    }

    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(movieDtoObj, MovieDto.class);
    }

    @GetMapping("v1/get-movie/{title}")
    @Operation(summary = "Gets a specific movie by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view movie"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<MovieDto> getMovieByTitleHandler(@PathVariable String title) throws MovieDoesNotExist {
        return new ResponseEntity<>(movieService.getMovie(title), HttpStatus.OK);
    }

    @GetMapping("v1/get-movies")
    @Operation(summary = "Gets all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movies"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view movies"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @PutMapping("v1/update-movie/{title}")
    @Operation(summary = "Updates a single movie details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie"),
            @ApiResponse(responseCode = "401", description = "Not authorized to view movie"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable String title,
                                                       @RequestPart String movieDto,
                                                       @RequestPart MultipartFile file)
            throws MovieDoesNotExist, IOException {
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>((MovieDto) movieService.updateMovieByTitle(title, dto, file), HttpStatus.OK);

    }

    @DeleteMapping
    @Operation(summary = "Deletes all movies in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the movies"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete all movies"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<String> deleteAllMoviesHandler(){
        return new ResponseEntity<>(movieService.deleteAllMovies(), HttpStatus.OK);
    }

    @DeleteMapping("v1/delete-movie/{title}")
    @Operation(summary = "Deletes movie by title in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the movie"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete movie"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<String> deleteMovieByTitleHandler(@PathVariable String title) throws MovieDoesNotExist {
        return new ResponseEntity<>(movieService.deleteMovieByTitle(title), HttpStatus.OK);
    }
}
