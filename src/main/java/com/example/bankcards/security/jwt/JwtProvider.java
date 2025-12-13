package com.example.bankcards.security.jwt;

import com.example.bankcards.security.UserDetailsImpls;
import io.jsonwebtoken.Claims;

/**
 * Интерфейс для работы с JWT: генерация токенов и извлечение данных из токена.
 */
public interface JwtProvider {

    /**
     * Генерирует JWT токен для пользователя.
     *
     * @param userDetailsImpls объект {@link UserDetailsImpls}, содержащий данные пользователя
     * @return сгенерированный JWT токен в виде строки
     */
    String generateToken(UserDetailsImpls userDetailsImpls);

    /**
     * Извлекает все claims (данные) из JWT токена.
     *
     * @param token JWT токен
     * @return объект {@link Claims} с данными токена
     */
    Claims extractClaims(String token);

    /**
     * Извлекает email из JWT токена.
     *
     * @param token JWT токен
     * @return username пользователя
     */
    String extractUsername(String token);
}
