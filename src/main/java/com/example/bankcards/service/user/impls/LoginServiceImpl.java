package com.example.bankcards.service.user.impls;

import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.user.LoginService;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.auxiliaryclasses.response.AuthResponse;
import com.example.bankcards.security.UserDetailsImpls;
import com.example.bankcards.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpls;
    private final JwtProvider JwtProvider;


    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpls, JwtProvider JwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpls = userDetailsServiceImpls;
        this.JwtProvider = JwtProvider;
    }

    @Override
    public AuthResponse authenticate(AuthAndRegisterRequest authAndRegisterRequest){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authAndRegisterRequest.getEmail(),
                        authAndRegisterRequest.getPassword())
        );

        UserDetailsImpls userDetailsImpls = userDetailsServiceImpls.loadUserByUsername(authAndRegisterRequest.getEmail());
        String token = JwtProvider.generateToken(userDetailsImpls);

        return new AuthResponse(token);
    }
}
