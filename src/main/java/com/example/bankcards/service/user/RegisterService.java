package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;

/**
 * Сервис для регистрации новых пользователей.
 */
public interface RegisterService {

    /**
     * Регистрирует нового пользователя по переданным данным.
     *
     * @param authAndRegisterRequest объект с данными для регистрации (логин, пароль)
     * @return {@link UserDTO} с информацией о зарегистрированном пользователе
     */
    UserDTO registr(AuthAndRegisterRequest authAndRegisterRequest);
}

