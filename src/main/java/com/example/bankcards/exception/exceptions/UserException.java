package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с пользователями
 */
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
