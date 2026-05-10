package com.astridback.api.domain.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity) {
        super(String.format("%s was not found", entity));
    }

    public NotFoundException(String entity, String key) {
        super(String.format("%s with %s was not found", entity, key));
    }

    public NotFoundException() {
        super("Not found");
    }
}
