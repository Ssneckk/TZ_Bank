package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с картами
 */
public class CardException extends RuntimeException {
    public CardException(String message) {
        super(message);
    }
}
