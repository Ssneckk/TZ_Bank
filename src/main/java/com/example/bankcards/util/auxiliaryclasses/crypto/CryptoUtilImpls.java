package com.example.bankcards.util.auxiliaryclasses.crypto;

import com.example.bankcards.exception.exceptions.CryptoUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Реализация {@link CryptoUtil}, отвечающая за шифрование и расшифровку
 * конфиденциальных данных (номера банковской карты).
 *
 * <p>Использует симметричное шифрование AES и секретный ключ,
 * загружаемый из конфигурации приложения.</p>
 *
 * <p><b>Важно:</b> предназначен для серверного шифрования данных
 * перед сохранением в базу данных.</p>
 */
@Component
public class CryptoUtilImpls implements CryptoUtil {

    private static final Logger log = LoggerFactory.getLogger(CryptoUtilImpls.class);

    private final SecretKey secretKey;

    public CryptoUtilImpls(@Value("${aes.secret-key}") String secretKeyValue) {
        this.secretKey = new SecretKeySpec(secretKeyValue.getBytes(), "AES");
    }

    /**
     * Шифрует входную строку с использованием алгоритма AES.
     *
     * @param card_number исходное значение (номер карты)
     * @return зашифрованное значение в Base64 формате
     * @throws CryptoUtilException если произошла ошибка при шифровании
     */
    @Override
    public String encrypt(String card_number)  {
        log.debug("Начало шифрования карты: длина номера = {}", card_number.length());

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(card_number.getBytes());
            String result = Base64.getEncoder().encodeToString(encrypted);
            log.debug("Шифрование карты успешно завершено");
            return result;
        }
        catch (Exception e) {
            log.error("Ошибка при шифровании номера карты", e);
            throw new CryptoUtilException("Произошла ошибка при кодировки номера карты!");
        }

    }

    /**
     * Расшифровывает ранее зашифрованную строку.
     *
     * @param encrypted_card_number зашифрованное значение в Base64 формате
     * @return расшифрованная исходная строка
     * @throws CryptoUtilException если произошла ошибка при расшифровке
     */
    @Override
    public String decrypt(String encrypted_card_number){
        log.debug("Начало расшифровки карты: длина зашифрованного номера = {}", encrypted_card_number.length());

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted_card_number));
            log.debug("Расшифровка карты успешно завершена");

            return new String(decrypted);
        }
        catch (Exception e) {
            log.error("Ошибка при расшифровке номера карты", e);
            throw new CryptoUtilException("Произошла ошибка при раскодировки номера карты!");
        }
    }
}
