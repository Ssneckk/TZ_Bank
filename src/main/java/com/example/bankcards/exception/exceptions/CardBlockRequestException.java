package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с запросами карт на блокировку
 */
public class CardBlockRequestException extends RuntimeException {
    public CardBlockRequestException(String message) {
        super(message);
    }
}
