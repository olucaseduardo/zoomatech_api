package com.credvip.credvip_mb.exceptions;

public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String key, String message) {
        super(message);
    }
}
