package com.artem.umbrella.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Entity does not exist");
    }
}
