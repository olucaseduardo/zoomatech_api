package com.olucaseduardo.zoomatech_api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}