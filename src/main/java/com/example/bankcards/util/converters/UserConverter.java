package com.example.bankcards.util.converters;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;

/**
 * Интерфейс для конвертации сущности {@link User} в DTO и обратно.
 */
public interface UserConverter {

    /**
     * Преобразует {@link User} в {@link UserDTO}.
     *
     * @param user пользователь для конвертации
     * @return DTO с информацией о пользователе
     */
    UserDTO convertToDTO(User user);

    /**
     * Преобразует {@link AuthAndRegisterRequest} в {@link User}.
     *
     * @param authAndRegisterRequest запрос с данными для регистрации/аутентификации
     * @return новая сущность {@link User}
     */
    User convertToEntity(AuthAndRegisterRequest authAndRegisterRequest);
}
