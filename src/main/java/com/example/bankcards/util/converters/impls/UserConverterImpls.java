package com.example.bankcards.util.converters.impls;

import com.example.bankcards.dto.RoleDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.converters.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Конвертер между сущностью {@link User} и её DTO представлением {@link UserDTO}.
 *
 * <p>Также поддерживает конвертацию {@link AuthAndRegisterRequest} в {@link User}
 * и {@link Role} в {@link RoleDTO}.</p>
 */
@Component
public class UserConverterImpls implements UserConverter {

    private static final Logger log = LoggerFactory.getLogger(UserConverterImpls.class);

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserConverterImpls(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Преобразует {@link User} в {@link UserDTO}.
     *
     * @param user пользователь для конвертации
     * @return DTO с id, email, статусом блокировки и ролями
     */
    @Override
    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setBlocked(user.getBlocked());

        //Безопасно конвертируем наш List<Role> в List<RoleDTO>
        List<RoleDTO> roleDTOs = user.getRoles() == null
                ? Collections.emptyList()
                : user.getRoles().stream().map(
                    this::convertToRoleDTO).toList();

        userDTO.setRoles(roleDTOs);

        log.debug("Конвертация User в UserDTO: id={}, email={}, blocked={}, roles={}",
                user.getId(), user.getEmail(), user.getBlocked(),
                roleDTOs.stream().map(RoleDTO::getRole_name).toList());

        return userDTO;
    }

    /**
     * Преобразует {@link AuthAndRegisterRequest} в сущность {@link User}.
     * <p>Пароль шифруется с помощью {@link PasswordEncoder}.</p>
     *
     * @param authAndRegisterRequest данные для регистрации пользователя
     * @return сущность {@link User}
     */
    @Override
    public User convertToEntity(AuthAndRegisterRequest authAndRegisterRequest) {
        User user = new User();
        user.setEmail(authAndRegisterRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authAndRegisterRequest.getPassword()));

        log.debug("Конвертация AuthAndRegisterRequest в User: email={}", authAndRegisterRequest.getEmail());

        return user;
    }

    /**
     * Преобразует {@link Role} в {@link RoleDTO}.
     *
     * @param role роль для конвертации
     * @return DTO с названием роли
     */
    public RoleDTO convertToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole_name(role.getRole_name());

        log.debug("Конвертация Role в RoleDTO: role_name={}", role.getRole_name());

        return roleDTO;
    }
}
