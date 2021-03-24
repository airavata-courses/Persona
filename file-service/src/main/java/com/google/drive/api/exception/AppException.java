package com.google.drive.api.exception;

public class AppException extends RuntimeException {

    private String message;

    public AppException(String message) {
        super(message);
        this.message = message;
    }
}
