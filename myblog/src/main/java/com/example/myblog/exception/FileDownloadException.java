package com.example.myblog.exception;

public class FileDownloadException extends RuntimeException{
    public FileDownloadException(String message){
        super(message);
    }
}
