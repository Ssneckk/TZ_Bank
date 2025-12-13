package com.example.bankcards.controller;

import com.example.bankcards.service.user.LoginService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.auxiliaryclasses.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Публичный REST-контроллер для:
 * аутентификации пользователей
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Аутентификация пользователя по форме
     * @param authAndRegisterRequest - форма логин и пароль
     * @return {@link AuthResponse} объект, содержащий JWT-токен для авторизации
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthAndRegisterRequest authAndRegisterRequest) {
        return ResponseEntity.ok(loginService.authenticate(authAndRegisterRequest));
    }
}
