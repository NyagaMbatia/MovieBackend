package com.joe.exception;

public class FileExistsException extends Throwable{
    public FileExistsException(String message){
        super(message);
    };
}
