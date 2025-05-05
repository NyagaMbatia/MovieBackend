package com.joe.exception;

public class MovieAlreadyExists extends RuntimeException {
    public MovieAlreadyExists(String message) {
        super(message);
    }
}
