package com.example.bankcards.service.user.impls;

import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.user.LoginService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.auxiliaryclasses.response.AuthResponse;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сервис аутентификации пользователей.
 *
 * <p>Отвечает за проверку учетных данных пользователя
 * и генерацию JWT-токена при успешной аутентификации.</p>
 *
 * <p>Используется при входе пользователя в систему.</p>
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpls;
    private final JwtProvider JwtProvider;


    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpls, JwtProvider JwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpls = userDetailsServiceImpls;
        this.JwtProvider = JwtProvider;
    }

    /**
     * Выполняет аутентификацию пользователя и возвращает JWT-токен.
     *
     * @param authAndRegisterRequest объект с учетными данными пользователя
     *                              (email и пароль)
     * @return {@link AuthResponse} объект, содержащий JWT-токен
     * @throws org.springframework.security.core.AuthenticationException
     *         если учетные данные некорректны
     */
    @Override
    public AuthResponse authenticate(AuthAndRegisterRequest authAndRegisterRequest){
        String email = authAndRegisterRequest.getEmail();
        log.info("LoginService: Попытка аутентификации пользователя с email: {}", email);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        authAndRegisterRequest.getPassword())
        );

        UserDetailsImpls userDetailsImpls = userDetailsServiceImpls.loadUserByUsername(authAndRegisterRequest.getEmail());
        String token = JwtProvider.generateToken(userDetailsImpls);

        log.info("LoginService: Пользователь успешно аутентифицирован: email={}",
                email);

        return new AuthResponse(token);
    }
}
