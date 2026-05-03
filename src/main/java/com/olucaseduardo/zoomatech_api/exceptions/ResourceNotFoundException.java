package com.olucaseduardo.zoomatech_api.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s não encontrado(a) com ID: %s", resource, id), HttpStatus.NOT_FOUND);
    }
}
