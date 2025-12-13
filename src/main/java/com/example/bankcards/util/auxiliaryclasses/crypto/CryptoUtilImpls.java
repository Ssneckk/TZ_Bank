package com.example.bankcards.util.auxiliaryclasses.crypto;

import com.example.bankcards.exception.exceptions.CryptoUtilException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CryptoUtilImpls implements CryptoUtil {

    private final SecretKey secretKey;

    public CryptoUtilImpls(@Value("${aes.secret-key}") String secretKeyValue) {
        this.secretKey = new SecretKeySpec(secretKeyValue.getBytes(), "AES");
    }

    @Override
    public String encrypt(String card_number)  {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(card_number.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch (Exception e) {
            throw new CryptoUtilException("Произошла ошибка при кодировки номера карты!");
        }

    }

    @Override
    public String decrypt(String encrypted_card_number){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted_card_number));
            return new String(decrypted);
        }
        catch (Exception e) {
            throw new CryptoUtilException("Произошла ошибка при раскодировки номера карты!");
        }
    }
}
