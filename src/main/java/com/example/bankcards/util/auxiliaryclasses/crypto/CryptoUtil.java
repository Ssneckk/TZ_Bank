package com.example.bankcards.util.auxiliaryclasses.crypto;

public interface CryptoUtil {

    String encrypt(String plaintext) throws Exception;

    String decrypt(String cipherText) throws Exception;
}
