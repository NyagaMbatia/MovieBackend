package com.joe.mapper;

import com.joe.dto.MovieDto;
import com.joe.entity.MovieEntity;
import org.springframework.stereotype.Component;

@Component
public class MovieDtoToEntityMapper {

    public MovieEntity dtoToEntity(MovieDto movieDto){
        return MovieEntity.builder()
                .director(movieDto.getDirector())
                .title(movieDto.getTitle())
                .poster(movieDto.getPoster())
                .studio(movieDto.getStudio())
                .posterURL(movieDto.getPosterURL())
                .movieCast(movieDto.getMovieCast())
                .releaseYear(movieDto.getReleaseYear())
                .build();
    }

    public MovieDto entityToDto(MovieEntity movieEntity){
        return MovieDto.builder()
                .director(movieEntity.getDirector())
                .id(movieEntity.getId())
                .title(movieEntity.getTitle())
                .movieCast(movieEntity.getMovieCast())
                .poster(movieEntity.getPoster())
                .posterURL(movieEntity.getPosterURL())
                .releaseYear(movieEntity.getReleaseYear())
                .studio(movieEntity.getStudio())
                .build();
    }
}
