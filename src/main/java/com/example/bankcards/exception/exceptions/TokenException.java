package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с JWT-токеном
 */
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
