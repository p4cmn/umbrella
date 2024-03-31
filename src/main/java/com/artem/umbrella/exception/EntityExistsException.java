package com.artem.umbrella.exception;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException() {
        super("Entity already exists");
    }
}
