package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для работы с пользователями.
 * <p>
 * Предоставляет методы для получения информации о пользователях и текущем пользователе.
 * </p>
 */
public interface UserService {

    /**
     * Возвращает информацию о пользователе по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return {@link UserDTO} с информацией о пользователе
     */
    UserDTO getInfo(Integer userId);

    /**
     * Возвращает страницу пользователей с пагинацией.
     *
     * @param pageable объект {@link Pageable} для пагинации
     * @return {@link Page} с {@link UserDTO} пользователей
     */
    Page<UserDTO> getUsers(Pageable pageable);

    /**
     * Возвращает идентификатор текущего аутентифицированного пользователя.
     *
     * @return идентификатор текущего пользователя
     */
    int getCurrentUserId();

    /**
     * Возвращает текущего аутентифицированного пользователя.
     *
     * @return {@link User} текущий пользователь
     */
    User findCurrentUser();
}

