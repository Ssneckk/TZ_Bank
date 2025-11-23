package com.example.bankcards.security.jwt;

import com.example.bankcards.util.auxiliaryclasses.UserDetailsImpls;

public interface JwtService {

    void validateToken(String token, UserDetailsImpls userDetailsImpls);
    String getUsernameFromToken(String token);
}
