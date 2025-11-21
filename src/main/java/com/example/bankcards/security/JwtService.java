package com.example.bankcards.security;

import com.example.bankcards.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


}
