package com.example.bankcards.service.user;

import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;
import com.example.bankcards.util.auxiliaryclasses.response.AuthResponse;

public interface LoginService {

    AuthResponse authenticate(AuthAndRegisterRequest authAndRegisterRequest);
}
