package com.example.bankcards.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Конфигурация Spring Security для приложения BankCards.
 *
 * Настраивает:
 * - цепочку безопасности (SecurityFilterChain),
 * - аутентификацию через JWT (jwtFilter),
 * - политику сессий Stateless,
 * - CORS для фронтенда на http://localhost:4200,
 * - PasswordEncoder и AuthenticationProvider.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final OncePerRequestFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, OncePerRequestFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Coздает PasswordEncoder для хеширования паролей пользователей
     * @return экземпляр PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //TODO: почему то при вводе неправильных данных ничего не выводится, надо исправить

    /**
     * Настройка конфигурации HTTP для корректной аутентификации и авторизации.
     * Здесь также внедряются пользовательские фильтры {@link OncePerRequestFilter} для дополнительной валидации JWT,
     * настраивается политика сессий Stateless и CORS для фронтенда.
     *
     * @param http объект HttpSecurity для конфигурации безопасности
     * @return сконфигурированную SecurityFilterChain
     * @throws Exception в случае ошибок конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login","/docs/**","/swagger-ui/**","/v3/api-docs/**")
                                .permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    /**
     * Настройка {@link AuthenticationProvider}, отвечающего за аутентификацию пользователей
     * на основе {@link UserDetailsService} и проверки паролей с использованием {@link PasswordEncoder}.
     *
     * Используется {@link DaoAuthenticationProvider} для аутентификации по логину и паролю.
     *
     * @return сконфигурированный AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Предоставляет {@link AuthenticationManager}, используемый для выполнения
     * аутентификации пользователей
     * @param authenticationConfiguration конфигурация Spring Security
     * @return  сконфигурированный AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Настройка CORS для ответа на preflight от браузера и
     * передачи разрешенных заголовков, HTTP-методов и credentials
     * @return сконфигурированный CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
