package com.example.bankcards.service.user.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сервисный слой для работы с пользователями.
 *
 * <p>Отвечает за получение информации о пользователях,
 * а также за работу с текущим аутентифицированным пользователем.</p>
 *
 * <p>Основные обязанности:</p>
 * <ul>
 *     <li>Получение информации о пользователе по идентификатору</li>
 *     <li>Получение списка пользователей с пагинацией</li>
 *     <li>Определение идентификатор текущего пользователя из SecurityContext</li>
 *     <li>Определение текущего пользователя из SecurityContext</li>
 * </ul>
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    /**
     * Возвращает информацию о пользователе по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return {@link UserDTO} DTO с информацией о пользователе
     * @throws UserException если пользователь не найден
     */
    @Override
    public UserDTO getInfo(Integer userId) {
        log.info("UserService: Запрошена информация о пользователе с id {}", userId);

        User user = findUserById(userId);
        log.debug("UserService: Информация о пользователе {} получена", userId);
        return userConverter.convertToDTO(user);
    }


    /**
     * Возвращает список пользователей с пагинацией
     *
     * @param pageable параметры пагинации
     * @return {@link Page} пользователей в виде {@link UserDTO}
     */
    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        log.info("UserService: Запрошен список пользователей с пагинацией: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<UserDTO> users = userRepository.findAll(pageable).map(userConverter::convertToDTO);
        log.debug("UserService: Получено {} пользователей", users.getNumberOfElements());
        return users;
    }

    /**
     * Возвращает идентификатор текущего аутентифицированного пользователя.
     *
     * @return идентификатор текущего пользователя
     * @throws ClassCastException если principal имеет неожиданный тип
     */
    @Override
    public int getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpls userDetails = (UserDetailsImpls) auth.getPrincipal();
        log.debug("Определен текущий пользователь: id={}", userDetails.getId());
        return userDetails.getId();
    }

    /**
     * Возвращает сущность текущего аутентифицированного пользователя.
     *
     * @return {@link User} текущий пользователь
     * @throws UserException если пользователь не найден
     */
    @Override
    public User findCurrentUser() {
        int userId = getCurrentUserId();
        log.info("Определение текущего пользователя с id {}", userId);
        return findUserById(userId);
    }

    public User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id {} не найден", userId);
                    return new UserException("User с id: " + userId + " не найден");
                });
    }
}
