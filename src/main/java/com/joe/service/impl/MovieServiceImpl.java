package com.joe.service.impl;

import com.joe.dto.MovieDto;
import com.joe.entity.MovieEntity;
import com.joe.exception.MovieAlreadyExists;
import com.joe.exception.MovieDoesNotExist;
import com.joe.mapper.MovieDtoToEntityMapper;
import com.joe.repository.MovieRepository;
import com.joe.service.FileService;
import com.joe.service.MovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Value("${project.poster}")
    public String path;

    @Value("${base.url}")
    public String baseUrl;

    private final FileService fileService;

    private final MovieRepository movieRepository;
    private final MovieDtoToEntityMapper mapper;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws MovieAlreadyExists, IOException {
        log.info("---------- add movie method call started -----------------------");
        // 1. Upload the file
        String uploadedFileName = fileService.uploadFileHandler(path, file);
        log.info("File name is : {}", uploadedFileName);

        // 2. Set the value poster as the filename
        movieDto.setPoster(uploadedFileName);

        // 3. Map Dto to movie object
         MovieEntity requestedMovie = mapper.dtoToEntity(movieDto);

        // 4. Save the movie object -> saved movie abject
        MovieEntity existingMovie = movieRepository.findByTitle(movieDto.getTitle());
        if (existingMovie != null)
            throw new MovieAlreadyExists("Movie with the same title exists, Please upload a new one");

        log.info("----------- saving movie --------------");
        MovieEntity savedMovie = movieRepository.save(requestedMovie);
        log.info("Saved movie title is : {}", savedMovie.getTitle());

        // 5. Generate poster URL
        String posterURL = baseUrl + "/api/file/v1/get-file/" + uploadedFileName;
        log.info("movie poster is located at {}", posterURL);

        // 6. Map movie entity to DTO and return it
        MovieDto response = mapper.entityToDto(savedMovie);
        response.setPosterURL(posterURL);

        return response;
    }

    @Override
    public MovieDto getMovie(String title) throws MovieDoesNotExist {
        log.info("--------------- Getting the movie -----------------");
        // 1. Get the movie details
        MovieEntity existingMovie = movieRepository.findByTitle(title);
        
        // 2. Check if the movie exists
        if(existingMovie == null)
            throw new MovieDoesNotExist("No movie title with that name exists!");

        // 3. Generate the poster URL
        String posterURL = baseUrl + "/api/file/v1/get-file/" + existingMovie.getPoster();
        
        // 3. Convert to dto
        MovieDto movieDto = mapper.entityToDto(existingMovie);
        movieDto.setPosterURL(posterURL);
        log.info("--------------- Retrieved movie : {}", existingMovie.getTitle());
        // 4. Return The dto object
        return movieDto;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        // 1. Fetch a list of movies from the database
        List<MovieEntity> allMovies = movieRepository.findAll();

        // 2. Iterate through the list, generate posterURL for each movie and finally return a list of
        // movie dto
        List<MovieDto> allMoviesDto = new ArrayList<>();
        for (MovieEntity movie : allMovies){
            String posterURL = baseUrl + "/api/file/v1/get-file/" + movie.getPoster();
            MovieDto movieDto = mapper.entityToDto(movie);
            movieDto.setPosterURL(posterURL);
            allMoviesDto.add(movieDto);
        }

        return allMoviesDto;
    }

    @Override
    public String deleteAllMovies() {
        movieRepository.deleteAll();
        return "All movies have been successfully deleted";
    }

    @Override
    public MovieDto updateMovieByTitle(String title, MovieDto movieDto, MultipartFile file) throws MovieDoesNotExist, IOException {
        // 1. Check if movie exists
        log.info("------------------ Checking if movie exists -------------");
        MovieEntity isExistingMovie = movieRepository.findByTitle(title);
        if(isExistingMovie == null)
            throw new MovieDoesNotExist("The movie your are trying to update does not exists, create one instead");

        Long id = isExistingMovie.getId();
        // 2. Check if file exists, else upload new
        String fileName = isExistingMovie.getPoster();
        if (file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileService.uploadFileHandler(path, file);
        }

        // 3. Generate movie posterURL
        String posterURL = baseUrl + "/api/file/v1/get-file/" + movieDto.getPoster();

        // 4. Update the particular movie with the details
        MovieEntity updatedMovie = mapper.dtoToEntity(movieDto);
        log.info("------------------ Updating movie -------------");
        updatedMovie.setId(id);
        movieRepository.save(updatedMovie);

        // 5. Map and Return our updated movieDTO
        MovieDto updatedMovieDto = mapper.entityToDto(updatedMovie);
        updatedMovieDto.setPosterURL(posterURL);
        log.info("Updated movie title : {}", updatedMovieDto.getTitle());
        return updatedMovieDto;
    }

    @Override
    public String deleteMovieByTitle(String title) throws MovieDoesNotExist {
        // 1. Check if movie with the provided title exists
        MovieEntity isExists = movieRepository.findByTitle(title);

        if (isExists == null){
            throw new MovieDoesNotExist("No movie with that title exists, Please create a new one");
        }

        // 2. Delete the movie
        movieRepository.deleteByTitle(title);
        log.info("------------ Movie {} deleted successfully ------------", title);
        return "Movie " + title + " deleted successfully";
    }
}
