package com.joe.service;

import com.joe.dto.MovieDto;
import com.joe.dto.MoviePageResponse;
import com.joe.exception.FileExistsException;
import com.joe.exception.MovieAlreadyExists;
import com.joe.exception.MovieDoesNotExist;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws MovieAlreadyExists, IOException, FileExistsException;
    MovieDto getMovie(String title) throws MovieDoesNotExist;
    List<MovieDto> getAllMovies();

    String deleteAllMovies();

    Object updateMovieByTitle(String title, MovieDto movieDto, MultipartFile file) throws MovieDoesNotExist, IOException, FileExistsException;

    String deleteMovieByTitle(String title) throws MovieDoesNotExist;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize,
                                                           String sortBy, String sortDir);
}
