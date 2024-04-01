package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateUniqueFieldException extends RuntimeException {
    public DuplicateUniqueFieldException(String message) {
        super(message);
    }
}
