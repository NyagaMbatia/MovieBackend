package com.joe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieDoesNotExist.class)
    public ProblemDetail handleMovieDoesNotExist(MovieDoesNotExist exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MovieAlreadyExists.class)
    public ProblemDetail handleMovieAlreadyExists(MovieAlreadyExists exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handleEmptyFileException(EmptyFileException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(FileExistsException.class)
    public ProblemDetail handleFileExistsException(FileExistsException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(CustomUsernameNotFoundException.class)
    public ProblemDetail handleCustomUsernameNotFoundException(CustomUsernameNotFoundException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ProblemDetail handleRefreshTokenExpiredException(RefreshTokenExpiredException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ProblemDetail handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
