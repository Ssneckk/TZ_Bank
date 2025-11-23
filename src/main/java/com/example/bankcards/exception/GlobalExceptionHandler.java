package com.example.bankcards.exception;

import com.example.bankcards.util.auxiliaryclasses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(404,e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException e) {
        return new ResponseEntity<>(
                new ErrorResponse(401, e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HashMap<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors()
                .forEach(error -> { errors.put(error.getObjectName(), error.getDefaultMessage());});

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
