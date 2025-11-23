package com.example.bankcards.security;

import com.example.bankcards.util.auxiliaryclasses.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtService;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);// убрать "Bearer "

        //!Важно здесь могут вылетать исключения если токен не валидный которые ловит RestControllerAdvice
        username = JwtService.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailsImpls userDetailsImpls = userDetailsService.loadUserByUsername(username);
            
            //Здесь проверва токена на пренадлежность хозяину
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
