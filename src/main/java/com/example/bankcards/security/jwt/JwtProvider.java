package com.example.bankcards.security.jwt;

import com.example.bankcards.util.auxiliaryclasses.UserDetailsImpls;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;

public interface JwtProvider {

    String generateToken(UserDetailsImpls userDetailsImpls);

    Claims extractClaims(String token);

    String extractUsername(String token);

    Integer extrackId(String token);

    List<SimpleGrantedAuthority> extractAuthorities(String token);

    Date extractExpiration(String token);
}
