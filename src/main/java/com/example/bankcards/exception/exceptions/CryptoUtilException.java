package com.example.bankcards.exception.exceptions;

/**
 * Исключения связанные с утилитой для шифрования и расшифрования номера карты
 */
public class CryptoUtilException extends RuntimeException {
    public CryptoUtilException(String message) {
        super(message);
    }
}
