package com.artem.umbrella.exception;

public class ImmunityException extends RuntimeException {

    public ImmunityException() {
        super("The immune system destroyed the virus");
    }
}
