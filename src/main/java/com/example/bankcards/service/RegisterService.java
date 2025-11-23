package com.example.bankcards.service;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;

public interface RegisterService {

    UserDTO registr(AuthAndRegistrRequest authAndRegistrRequest);
}
