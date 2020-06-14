package com.creedowl.gallery.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
}
