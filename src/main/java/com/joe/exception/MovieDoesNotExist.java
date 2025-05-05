package com.joe.exception;

public class MovieDoesNotExist extends RuntimeException {
    public MovieDoesNotExist(String message) {
        super(message);
    }
}
