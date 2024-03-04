package com.artem.umbrella.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class ImmunityException extends RuntimeException {

    public ImmunityException() {
        super("The immune system destroyed the virus");
    }
}
