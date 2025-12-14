package com.example.bankcards.service.user.impls;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.exceptions.UserException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.converters.UserConverter;
import com.example.bankcards.service.user.RegisterService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сервис регистрации пользователей.
 *
 * <p>Отвечает за создание нового пользователя на основе входных данных,
 * проверку уникальности email и сохранение пользователя в системе.</p>
 *
 * <p>При успешной регистрации пользователю назначается базовая роль.</p>
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

    private static final Logger log = LoggerFactory.getLogger(RegisterServiceImpl.class);

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    public RegisterServiceImpl(UserConverter userConverter, UserRepository userRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param authAndRegisterRequest данные для регистрации
     *                              (email, пароль)
     * @return {@link UserDTO} DTO зарегистрированного пользователя
     * @throws UserException если пользователь с таким email уже существует
     */
    @Override
    @Transactional
    public UserDTO registr(AuthAndRegisterRequest authAndRegisterRequest) {
        String email = authAndRegisterRequest.getEmail();
        log.info("Начало регистрации пользователя с email: {}", email);

        if(userRepository.findByEmail(email).isPresent()) {
            log.warn("Попытка регистрации с уже существующим email: {}", email);
            throw new UserException("User c таким Email: "
                    + email + " уже существует");
        }

        User user = userConverter.convertToEntity(authAndRegisterRequest);
        User newUser = userRepository.save(user);

        Role role = new Role();
        List<Role> userRole = new ArrayList<>();
        userRole.add(role);
        role.setUser(newUser);
        newUser.setRoles(userRole);

        log.info("Пользователь успешно зарегистрирован: email={}, id={}", email, newUser.getId());

        return userConverter.convertToDTO(newUser);
    }
}
