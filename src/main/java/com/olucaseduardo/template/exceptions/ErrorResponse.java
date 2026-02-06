package com.olucaseduardo.template.exceptions;

public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String key, String message) {
        super(message);
    }
}
