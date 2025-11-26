package com.example.bankcards.security.jwt.filter;

import com.example.bankcards.exception.TokenException;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.security.jwt.JwtService;
import com.example.bankcards.util.auxiliaryclasses.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService JwtService;

    @Autowired
    public JwtFilter(UserDetailsServiceImpl userDetailsService, JwtService JwtService) {
        this.userDetailsService = userDetailsService;
        this.JwtService = JwtService;
    }

    //TODO: нужно сделать так что бы были понятные ошибки при неправильном токене иначе никакого респонса нет

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        String username = "";

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);// убрать "Bearer "

        //!Важно здесь могут вылетать исключения если токен не валидный которые ловит RestControllerAdvice
        username = JwtService.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailsImpls userDetailsImpls = userDetailsService.loadUserByUsername(username);
            
            //Здесь проверка токена на принадлежность владельцу
            JwtService.validateToken(token, userDetailsImpls);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetailsImpls,
                            null,
                            userDetailsImpls.getAuthorities()
                        );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        filterChain.doFilter(request, response);
    }
}
