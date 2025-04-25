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

import java.io.IOException;
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
        if (!(existingMovie == null))
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
        
        // 3. Convert to dto
        MovieDto movieDto = mapper.entityToDto(existingMovie);
        log.info("--------------- Retrieved movie : {}", existingMovie);
        // 4. Return The dto object
        return movieDto;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<MovieEntity> allMovies = movieRepository.findAll();

        // List<MovieDto> allMoviesDto =
        return List.of();
    }
}
