package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для интеграции сущности {@link User} с Spring Security.
 * <p>
 * Реализует {@link UserDetailsService} и отвечает за загрузку пользователя
 * по email для процесса аутентификации.
 * </p>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Сервис для интеграции сущности {@link User} с Spring Security.
     * <p>
     * Реализует {@link UserDetailsService} и отвечает за загрузку пользователя
     * по email для процесса аутентификации.
     * </p>
     */
    @Override
    public UserDetailsImpls loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(email +" не найден!"));
        return new UserDetailsImpls(user);
    }
}
