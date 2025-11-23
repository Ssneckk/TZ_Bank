package com.example.bankcards.security.jwtimpls;

import com.example.bankcards.exception.TokenException;
import com.example.bankcards.util.auxiliaryclasses.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtProvider;
import com.example.bankcards.security.jwt.JwtService;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProvider JwtProvider;

    public JwtServiceImpl(JwtProvider JwtProvider) {
        this.JwtProvider = JwtProvider;
    }

    @Override
    public void validateToken(String token, UserDetailsImpls userDetailsImpls) {
        String username = JwtProvider.extractUsername(token);

        if (!username.equals(userDetailsImpls.getUsername())) {
            throw new TokenException("Токен не соответствует пользователю");
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return JwtProvider.extractUsername(token);
    }
}
