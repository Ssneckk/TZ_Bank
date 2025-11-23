package com.example.bankcards.service;

import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;
import com.example.bankcards.util.auxiliaryclasses.AuthResponse;

public interface LoginService {

    AuthResponse authenticate(AuthAndRegistrRequest authAndRegistrRequest);
}
