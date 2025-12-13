package com.example.bankcards.util.auxiliaryclasses.crypto;

public interface CryptoUtil {

    String encrypt(String plaintext);

    String decrypt(String cipherText);
}
