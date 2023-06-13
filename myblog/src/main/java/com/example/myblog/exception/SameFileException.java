package com.example.myblog.exception;

public class SameFileException extends RuntimeException{
    public SameFileException(String message){
        super(message);
    }
}
