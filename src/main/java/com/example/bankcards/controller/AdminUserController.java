package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.user.BlockUserService;
import com.example.bankcards.service.user.RegisterService;
import com.example.bankcards.service.user.UserService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST - контроллер с административными операциями управления пользователями.
 * <p>Предоставляет функции для:</p>
 * <ul>
 *      <li>Регистрации пользователей;</li>
 *      <li>Получения всех пользователей;</li>
 *      <li>Получения информации аккаунта пользователя;</li>
 *      <li>Блокировки пользователя;</li>
 * </ul>
 */

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final RegisterService registerService;
    private final UserService userService;
    private final BlockUserService blockUserService;
    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    public AdminUserController(RegisterService registerService, UserService userService, BlockUserService blockUserService) {
        this.registerService = registerService;
        this.userService = userService;
        this.blockUserService = blockUserService;
    }

    /**
     * Регистрирует пользователя по данным формы.
     *
     * @param authAndRegisterRequest форма с логином и паролем
     * @return {@link UserDTO} DTO зарегистрированного пользователя
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody AuthAndRegisterRequest authAndRegisterRequest) {
        log.info("AdminUserController: ADMIN запросил регистрацию нового пользователя с email={}", authAndRegisterRequest.getEmail());

        UserDTO registeredUser = registerService.registr(authAndRegisterRequest);
        log.debug("AdminUserController: Зарегистрирован пользователь: {}", registeredUser);

        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Получение списка пользователей с пагинацией и сортировкой
     * @param pageable - параметры пагинации и сортировки
     * @return страница со списков {@link UserDTO} DTO пользователей
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDTO>> getUsers(Pageable pageable) {
        log.info("AdminUserController: ADMIN запросил список пользователей. Страница={}, размер={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<UserDTO> users = userService.getUsers(pageable);
        log.debug("AdminUserController: Количество пользователей на странице: {}", users.getContent().size());

        return ResponseEntity.ok(users);
    }

    /**
     * Получение подробной информации о пользователе по идентификатору
     * @param userId - идентификатор пользователя
     * @return {@link UserDTO} DTO пользователя
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getAccountInfo(@PathVariable Integer userId) {
        log.info("AdminUserController: ADMIN запросил информацию по пользователю id={}", userId);

        UserDTO userDTO = userService.getInfo(userId);
        log.debug("AdminUserController: Данные пользователя: {}", userDTO);

        return ResponseEntity.ok(userDTO);
    }

    /**
     * Блокирует или разблокирует пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя
     * @param blockOrNot true для блокировки, false для разблокировки
     * @return {@link Map} сообщение с результатом блокировки/разблокировки пользователя
     */
    @PatchMapping("/{userId}/block")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String,String>> block(@PathVariable Integer userId, @RequestParam Boolean blockOrNot) {
        String action = blockOrNot ? "Блокировка" : "Разблокировка";
        log.info("AdminUserController: ADMIN запросил {} пользователя id={}", action, userId);

        Map<String,String> result = blockUserService.block(userId, blockOrNot);
        log.debug("AdminUserController: {} пользователя id={} завершена. Результат: {}", action, userId, result);

        return ResponseEntity.ok(result);
    }
}
