package com.example.bankcards.service;

import com.example.bankcards.dto.UserDTO;

public interface UserInfoService {

    UserDTO getInfo(String authHeader);

}
