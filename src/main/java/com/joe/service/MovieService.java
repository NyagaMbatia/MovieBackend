package com.joe.service;

import com.joe.dto.MovieDto;
import com.joe.exception.MovieAlreadyExists;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws MovieAlreadyExists, IOException;
    MovieDto getMovie(String title);
    List<MovieDto> getAllMovies();
}
