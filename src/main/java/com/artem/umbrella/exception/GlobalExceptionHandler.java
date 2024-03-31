package com.artem.umbrella.exception;

import com.artem.umbrella.dto.ResponseDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception) {
        var message = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("; "));
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(message)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ResponseDto> handleEntityNotFoundException(final EntityNotFoundException exception) {
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ResponseDto> handleEntityExistsException(final EntityExistsException exception) {
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ResponseDto> handleMethodNotAllowed(
            final HttpRequestMethodNotSupportedException exception) {
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(responseDto);
    }

    @ExceptionHandler(ImmunityException.class)
    protected ResponseEntity<ResponseDto> handleImmunityException(final ImmunityException exception) {
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.I_AM_A_TEAPOT)
                .body(responseDto);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResponseDto> handleInternalServerError(final Exception exception) {
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseDto);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<ResponseDto> handleNotImplemented(final HttpMediaTypeNotAcceptableException exception) {
        var responseDto = ResponseDto.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(responseDto);
    }
}
