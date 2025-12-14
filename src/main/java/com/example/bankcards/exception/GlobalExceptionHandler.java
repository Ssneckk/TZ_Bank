package com.example.bankcards.exception;

import com.example.bankcards.exception.exceptions.*;
import com.example.bankcards.exception.exceptions.SecurityException;
import com.example.bankcards.util.auxiliaryclasses.response.ErrorResponse;
import com.example.bankcards.util.auxiliaryclasses.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST-контроллеров приложения.
 * <p>Перехватывает различные исключения и возвращает корректные HTTP-статусы
 * вместе с подробными сообщениями об ошибках в формате JSON.</p>
 *
 * Поддерживаемые исключения:
 * <ul>
 *     <li>{@link UsernameNotFoundException} - 404 NOT FOUND с сообщением ошибки</li>
 *     <li>{@link TokenException} - 401 UNAUTHORIZED с сообщением ошибки</li>
 *     <li>{@link UserException} - 400 BAD REQUEST с сообщением ошибки</li>
 *     <li>{@link CardException} - 400 BAD REQUEST с сообщением ошибки</li>
 *     <li>{@link CardBlockRequestException} - 400 BAD REQUEST с сообщением ошибки</li>
 *     <li>{@link TransferException} - 400 BAD REQUEST с сообщением ошибки</li>
 *     <li>{@link CryptoUtilException} - 400 BAD REQUEST с сообщением ошибки</li>
 *     <li>{@link LockedException} - 401 UNAUTHORIZED с сообщением ошибки</li>
 *     <li>{@link SecurityException} - 401 UNAUTHORIZED с сообщением ошибки</li>
 *     <li>{@link MethodArgumentNotValidException} - 400 BAD REQUEST с деталями ошибок валидации</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ------------------ 404 NOT FOUND ------------------
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("UsernameNotFoundException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(404,e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    // ------------------ 401 UNAUTHORIZED ------------------
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException e) {
        log.warn("TokenException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(401, e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException e) {
        log.warn("LockedException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(401, "Аккаунт заблокирован"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(SecurityException e) {
        log.warn("SecurityException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(401, e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // ------------------ 400 BAD REQUEST ------------------
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserException e) {
        log.warn("UserException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CardException.class)
    public ResponseEntity<ErrorResponse> handleCardException(CardException e) {
        log.warn("CardException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: validation failed");

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse(400,"Ошибка валидации", errors);

        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CardBlockRequestException.class)
    public ResponseEntity<ErrorResponse> handleCardBlockRequestException(CardBlockRequestException e) {
        log.warn("CardBlockRequestException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<ErrorResponse> handleCardBlockRequestException(TransferException e) {
        log.warn("TransferException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CryptoUtilException.class)
    public ResponseEntity<ErrorResponse> handleCryptoUtilException(CryptoUtilException e) {
        log.warn("CryptoUtilException: {}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(400, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
