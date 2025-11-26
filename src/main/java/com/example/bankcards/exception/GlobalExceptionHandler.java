package com.example.bankcards.exception;

import com.example.bankcards.util.auxiliaryclasses.response.ErrorResponse;
import com.example.bankcards.util.auxiliaryclasses.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserException e) {
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CardException.class)
    public ResponseEntity<ErrorResponse> handleCardException(CardException e) {
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse(400,"Ошибка валидации", errors);


        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CardBlockRequestException.class)
    public ResponseEntity<ErrorResponse> handleCardBlockRequestException(CardBlockRequestException e) {
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<ErrorResponse> handleCardBlockRequestException(TransferException e) {
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException e) {
        return new ResponseEntity<>(
                new ErrorResponse(401, "Аккаунт заблокирован"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException e) {
        return new ResponseEntity<>(
                new ErrorResponse(401, e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }
}
