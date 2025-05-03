package com.joe.service;

import com.joe.dto.MovieDto;
import com.joe.exception.MovieAlreadyExists;
import com.joe.exception.MovieDoesNotExist;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws MovieAlreadyExists, IOException;
    MovieDto getMovie(String title) throws MovieDoesNotExist;
    List<MovieDto> getAllMovies();

    String deleteAllMovies();

    Object updateMovieByTitle(String title, MovieDto movieDto, MultipartFile file) throws MovieDoesNotExist, IOException;

    String deleteMovieByTitle(String title) throws MovieDoesNotExist;
}
