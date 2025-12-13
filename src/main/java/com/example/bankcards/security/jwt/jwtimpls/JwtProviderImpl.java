package com.example.bankcards.security.jwt.jwtimpls;

import com.example.bankcards.config.JwtProperties;
import com.example.bankcards.exception.exceptions.TokenException;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link JwtProvider} для генерации и парсинга JWT токенов.
 * <p>Использует {@link JwtProperties} для получения секретного ключа и времени жизни токена.</p>
 */
@Component
public class JwtProviderImpl implements JwtProvider {

    private final JwtProperties jwtProperties;

    public JwtProviderImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Генерирует JWT токен для пользователя с префиксом "Bearer ".
     *
     * @param userDetailsImpls объект {@link UserDetailsImpls}, для которого создается токен
     * @return строка JWT токена с префиксом "Bearer "
     */
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

    /**
     * Извлекает Claims (payload) из JWT токена.
     * <p>Если токен недействителен или просрочен, выбрасывает {@link TokenException} с описанием причины.</p>
     *
     * @param authHeader строка заголовка авторизации, содержащая токен с префиксом "Bearer "
     * @return объект {@link Claims} содержащий данные токена
     * @throws TokenException если токен просрочен, поврежден, пустой или имеет неверную подпись
     */
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

    /**
     * Извлекает username (обычно email) пользователя из токена.
     *
     * @param token JWT токен с префиксом "Bearer "
     * @return username пользователя
     * @throws TokenException если токен недействителен
     */
    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

}
