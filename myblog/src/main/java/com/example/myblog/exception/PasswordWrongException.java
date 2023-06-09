package com.example.myblog.exception;

public class PasswordWrongException extends RuntimeException{
    public PasswordWrongException(String message){
        super(message);
    }
}
