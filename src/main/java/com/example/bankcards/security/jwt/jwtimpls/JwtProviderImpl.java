package com.example.bankcards.security.jwt.jwtimpls;

import com.example.bankcards.config.JwtProperties;
import com.example.bankcards.exception.TokenException;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final JwtProperties jwtProperties;

    public JwtProviderImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generateToken(UserDetailsImpls userDetailsImpls) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+jwtProperties.getExpiration());

        return "Bearer "+Jwts.builder()
                .subject(userDetailsImpls.getUsername())
                .claim("authorities", userDetailsImpls.getAuthorities())
                .claim("id", userDetailsImpls.getId())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtProperties.getSecretKey())
                .compact();
    }

    @Override
    public Claims extractClaims(String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        try{
            Claims claims = Jwts.parser()
                    .verifyWith(jwtProperties.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims;
        }
        catch (ExpiredJwtException e) {
            throw new TokenException("Токен просрочен: " + e.getMessage());
        }
        catch (MalformedJwtException e){
            throw new TokenException("Токен поврежден: " + e.getMessage());
        }
        catch (SignatureException e){
            throw new TokenException("Неверная подпись: " + e.getMessage());
        }
        catch (UnsupportedJwtException e){
            throw new TokenException("Неподдерживаемый JWT тип: " + e.getMessage());
        }
        catch (IllegalArgumentException e){
            throw new TokenException("Пустой токен: " + e.getMessage());
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public Integer extrackId(String token) {
        return extractClaims(token).get("id", Integer.class);
    }

    @Override
    public List<SimpleGrantedAuthority> extractAuthorities(String token) {

        List<String> roles = extractClaims(token).get("authorities", List.class);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

}
