package org.example.exception;

import org.example.dto.error.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    protected ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        return createResponseEntity(
                new ErrorDto(LocalDateTime.now(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateUniqueFieldException.class)
    protected ResponseEntity<Object> handleDuplicateUniqueFieldException(DuplicateUniqueFieldException ex) {
        return createResponseEntity(
                new ErrorDto(LocalDateTime.now(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createResponseEntity(ErrorDto errorDto, HttpStatus status) {
        return new ResponseEntity<>(errorDto, status);
    }
}
