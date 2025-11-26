package com.example.bankcards.service.user;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.util.auxiliaryclasses.request.AuthAndRegisterRequest;

public interface RegisterService {

    UserDTO registr(AuthAndRegisterRequest authAndRegisterRequest);
}
