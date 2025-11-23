package com.example.bankcards.util.converters;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.auxiliaryclasses.AuthAndRegistrRequest;

public interface UserConverter {

    UserDTO convertToDTO(User user);

    User convertToUser(AuthAndRegistrRequest authAndRegistrRequest);
}
