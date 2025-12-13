package com.example.bankcards.security.jwt.filter;

import com.example.bankcards.exception.GlobalExceptionHandler;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.security.jwt.JwtProvider;
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
    private final JwtProvider jwtProvider;

    @Autowired
    public JwtFilter(UserDetailsServiceImpl userDetailsService, JwtProvider jwtProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
    }

    //TODO: нужно сделать так что бы были понятные ошибки при неправильном токене иначе никакого респонса нет

    /**
     * Основной метод фильтрации HTTP-запросов для аутентификации через JWT.
     * <p>
     * Проверяет наличие заголовка "Authorization", извлекает токен, валидирует его
     * и устанавливает объект {@link UsernamePasswordAuthenticationToken} в контекст безопасности Spring.
     * В случае отсутствия токена или некорректного формата запрос передается дальше по цепочке {@link FilterChain}.
     * </p>
     *
     * <p>Важно: здесь могут возникать исключения при невалидном токене,
     * которые обрабатываются глобальным {@link GlobalExceptionHandler}.</p>
     *
     * @param request входящий HTTP-запрос {@link HttpServletRequest}
     * @param response HTTP-ответ {@link HttpServletResponse}
     * @param filterChain цепочка фильтров {@link FilterChain}
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException если возникает ошибка ввода/вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        String username = "";

        //Проверка заголовка на наличие JWT токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);// убрать "Bearer "

        //!Важно здесь могут вылетать исключения если токен не валидный которые ловит RestControllerAdvice
        username = jwtProvider.extractUsername(token);

        // Если JWT не пустой, но пользователь не авторизирован, значит приступаем к авторизации
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailsImpls userDetailsImpls = userDetailsService.loadUserByUsername(username);

            //создание токена для авторизации
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
