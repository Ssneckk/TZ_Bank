package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с безопасностью
 */
public class SecurityException extends RuntimeException {
    public SecurityException(String message) {
        super(message);
    }
}
