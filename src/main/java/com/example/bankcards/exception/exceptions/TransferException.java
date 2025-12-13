package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с переводами
 */
public class TransferException extends RuntimeException {
    public TransferException(String message) {
        super(message);
    }
}
