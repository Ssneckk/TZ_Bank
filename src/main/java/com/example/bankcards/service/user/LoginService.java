package com.example.bankcards.service.user;

import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.auxiliaryclasses.response.AuthResponse;

/**
 * Сервис для аутентификации пользователей.
 */
public interface LoginService {

    /**
     * Аутентифицирует пользователя по переданным учетным данным.
     *
     * @param authAndRegisterRequest объект с логином и паролем пользователя
     * @return {@link AuthResponse} с токеном и информацией о пользователе
     */
    AuthResponse authenticate(AuthAndRegisterRequest authAndRegisterRequest);
}

