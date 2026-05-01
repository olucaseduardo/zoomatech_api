package com.olucaseduardo.zoomatech_api.exceptions;

public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String key, String message) {
        super(message);
    }
}
