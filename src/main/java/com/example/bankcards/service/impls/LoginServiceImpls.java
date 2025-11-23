package com.example.bankcards.service.impls;

import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.LoginService;
import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;
import com.example.bankcards.util.auxiliaryclasses.AuthResponse;
import com.example.bankcards.util.auxiliaryclasses.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpls implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpls;
    private final JwtProvider JwtProvider;


    @Autowired
    public LoginServiceImpls(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpls, JwtProvider JwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpls = userDetailsServiceImpls;
        this.JwtProvider = JwtProvider;
    }

    @Override
    public AuthResponse authenticate(AuthAndRegistrRequest authAndRegistrRequest){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authAndRegistrRequest.getEmail(),
                        authAndRegistrRequest.getPassword())
        );

        UserDetailsImpls userDetailsImpls = userDetailsServiceImpls.loadUserByUsername(authAndRegistrRequest.getEmail());
        String token = JwtProvider.generateToken(userDetailsImpls);

        return new AuthResponse(token);
    }
}
