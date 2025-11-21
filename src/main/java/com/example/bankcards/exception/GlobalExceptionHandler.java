package com.example.bankcards.exception;

import com.example.bankcards.util.auxiliaryclasses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(404,e.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
