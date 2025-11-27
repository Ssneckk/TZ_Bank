package com.example.bankcards.service.user.impls;

import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.service.user.LoginService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.auxiliaryclasses.response.AuthResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginServiceImplTest {

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsServiceImpls;
    private JwtProvider jwtProvider;

    private LoginService loginService;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        userDetailsServiceImpls = mock(UserDetailsServiceImpl.class);
        jwtProvider = mock(JwtProvider.class);
        loginService = new LoginServiceImpl(authenticationManager, userDetailsServiceImpls, jwtProvider);
    }

    @Test
    void authenticate_shouldThrowUsernameNotFoundException() throws UsernameNotFoundException {

        AuthAndRegisterRequest authAndRegisterRequest = new AuthAndRegisterRequest();
        authAndRegisterRequest.setEmail("email@email.com");
        String expectedMessage = authAndRegisterRequest.getEmail() + " не найден!";

        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));

        when(userDetailsServiceImpls.loadUserByUsername(authAndRegisterRequest.getEmail())).thenThrow(new UsernameNotFoundException(expectedMessage));

        UsernameNotFoundException usernameNotFoundException =
                assertThrows(UsernameNotFoundException.class, () -> loginService.authenticate(authAndRegisterRequest));

        assertEquals(expectedMessage,usernameNotFoundException.getMessage());
    }

    @Test
    void authenticate_shouldReturnToken(){

        AuthAndRegisterRequest authAndRegisterRequest = new AuthAndRegisterRequest();
        authAndRegisterRequest.setEmail("email@email.com");
        authAndRegisterRequest.setPassword("password");

        UserDetailsImpls userDetailsImpls = new UserDetailsImpls();
        String token = "token321";

        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));

        when(userDetailsServiceImpls.loadUserByUsername(authAndRegisterRequest.getEmail()))
                .thenReturn(userDetailsImpls);

        when(jwtProvider.generateToken(userDetailsImpls)).thenReturn(token);

        AuthResponse authResponse = loginService.authenticate(authAndRegisterRequest);

        assertEquals(token,authResponse.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsServiceImpls).loadUserByUsername(authAndRegisterRequest.getEmail());
        verify(jwtProvider).generateToken(userDetailsImpls);
    }
}