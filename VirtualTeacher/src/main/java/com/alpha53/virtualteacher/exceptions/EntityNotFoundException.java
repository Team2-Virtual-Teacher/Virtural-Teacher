package com.alpha53.virtualteacher.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String type, int id) {
        this(type, "id", String.valueOf(id));
    }

    public EntityNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }

    public EntityNotFoundException(String type, String value) {
        super(String.format("%s with %s not found.", type, value));
    }

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
